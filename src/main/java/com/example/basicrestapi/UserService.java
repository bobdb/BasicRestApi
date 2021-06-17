package com.example.basicrestapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.basicrestapi.StatusName.FAILURE;
import static com.example.basicrestapi.StatusName.SUCCESS;

/**
 * A Service to provide User db access as well as functions to perform User-specific actions
 * such as adding (funding) and subtracting (transferring) Reward Points from their balance
 */
@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PointtransferRepository pointTransferRepository;

    // Static list of three Loyalty Programs
    // NOTE:  this is a place-holder.  A User's subscriptions should be stored in a column
    //        in the the User table.
    private static final List<Program> TEST_REWARDS_PROGRAMS = new ArrayList<>();
    static {
        TEST_REWARDS_PROGRAMS.add(new Program(1, "Company1", 2));
        TEST_REWARDS_PROGRAMS.add(new Program(2, "Company2", 3));
        TEST_REWARDS_PROGRAMS.add(new Program(3, "Company3", .5));
    }

    // Create a map to easily access a particular Loyalty Program's point exchange rate
    private static final Map<String,Double> PROGRAM_EXCHANGERATE_MAP = new HashMap<>();
    static {
        for (Program p: TEST_REWARDS_PROGRAMS) {
            PROGRAM_EXCHANGERATE_MAP.put(p.getName(),p.getExchangeRate());
        }
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(long id) {
        return userRepository.findById(id);
    }

    public List<Program> findProgramsById(long id) {
        return getPrograms(id);
    }

    public List<Pointtransfer> findPointTransfersById(long id) {
        return pointTransferRepository.findByUserId(id);
    }

    /**
     *
     * @param userId - the User that is receiving points
     * @param pointTransfer - the Pointtransfer object describing where the points are going,
     *                        how many to transfer, and some basic accounting information
     * @return
     */
    public FundingResponse fundAccount(long userId, Pointtransfer pointTransfer) {

        // Get user from database
        User user = findById(userId);

        // Partial build of Response
        StringBuilder sb = new StringBuilder();
        sb.append("UserId \'")
                .append(pointTransfer.getUserId())
                .append("\' add ")
                .append(pointTransfer.getAmount())
                .append(" points to account");
        FundingResponse response = new FundingResponse(userId, sb.toString());
        response.setStartingBalance(user.getPoints());
        response.setAmount(pointTransfer.getAmount());

        // Set the starting balance
        pointTransfer.setStartingBalance(user.getPoints());

        //Validation - The number of points needs to be at least 1
        if (pointTransfer.getAmount()<1) {
            response.setEndingBalance(user.getPoints());
            response.setMessage("Bad funding");
            response.setStatus(FAILURE);

            pointTransfer.setStatus(FAILURE.name());
            pointTransferRepository.save(pointTransfer);

            return response;
        }



        // Update the balance after adding points
        long newBalance = user.getPoints() + pointTransfer.getAmount();
        pointTransfer.setEndingBalance(newBalance);


        //  Persist to the pointtransfer repository
        //  NOTE:  This transaction is assumed to succeed.  In production, contingencies would be
        //         made to check and rollback if necessary
        pointTransfer.setStatus(SUCCESS.name());
        pointTransferRepository.save(pointTransfer);

        //  Update the User Point balance and persist
        //  NOTE:  This transaction is assumed to succeed.  See above.
        user.setPoints(newBalance);
        userRepository.save(user);

        // Complete the Response
        response.setEndingBalance(newBalance);
        response.setStatus(SUCCESS);

        return response;
    }

    /**
     *
     * @param userId - the User that is transferring points from their account
     * @param pointTransfer - the Pointtransfer object describing where the points are going,
     *                        how many to transfer, and some basic accounting information
     * @return
     */
    public FundingResponse transferPoints(long userId, Pointtransfer pointTransfer) {
        // Get user from database
        User user = findById(userId);

        FundingResponse response = new FundingResponse();
        response.setId(userId);
        response.setStartingBalance(user.getPoints());

        StringBuilder sb = new StringBuilder();
        sb.append("UserId \'")
                .append(userId)
                .append("\' transfer ")
                .append(pointTransfer.getAmount())
                .append(" points to ")
                .append(pointTransfer.getDestination());
        pointTransfer.setUserId(userId);
        pointTransfer.setMessage(sb.toString());
        pointTransfer.setStartingBalance(user.getPoints());
        pointTransfer.setExchangeRate(PROGRAM_EXCHANGERATE_MAP.get(pointTransfer.getDestination()));

        //Validation - The number of points needs to be at least 1
        if (pointTransfer.getAmount()<1) {
            pointTransfer.setStatus(FAILURE.name());
            pointTransfer.setEndingBalance(user.getPoints());
            String msg = "failed: The number of points needs to be at least 1 {"+ pointTransfer.getMessage() + "}";
            pointTransfer.setMessage(msg);
            pointTransferRepository.save(pointTransfer);
            response.setStatus(FAILURE);
            response.setEndingBalance(user.getPoints());
            response.setAmount(pointTransfer.getAmount());
            response.setMessage(msg);
            return response;
        }

        //Validation - Are there enough available points to make the transfer?
        if (user.getPoints()<pointTransfer.getAmount()) {
            pointTransfer.setStatus(FAILURE.name());
            String msg = "failed: Not enough points for transfer {" + pointTransfer.getMessage() +"}";
            pointTransfer.setMessage(msg);
            pointTransferRepository.save(pointTransfer);
            response.setStatus(FAILURE);
            response.setEndingBalance(user.getPoints());
            response.setAmount(pointTransfer.getAmount());
            response.setMessage(msg);
            return response;
        }

        //Validation - Is the user subscribed to the requested rewards program?
        if (!PROGRAM_EXCHANGERATE_MAP.containsKey(pointTransfer.getDestination())) {
            pointTransfer.setStatus(FAILURE.name());
            String msg = "failed: User not subscribed {" + pointTransfer.getMessage() + "}";
            pointTransfer.setMessage(msg);
            pointTransferRepository.save(pointTransfer);
            response.setStatus(FAILURE);
            response.setEndingBalance(user.getPoints());
            response.setAmount(pointTransfer.getAmount());
            response.setMessage(msg);
            return response;
        }

        //Validation - The receiving party's number of points after performing the exchange rate must be >= 1
        double exchangeRate = PROGRAM_EXCHANGERATE_MAP.get(pointTransfer.getDestination());
        if (exchangeRate * pointTransfer.getAmount() < 1) {
            pointTransfer.setStatus(FAILURE.name());
            String msg = "failed: Post-exchange rate amount too low {" + pointTransfer.getMessage() + "}";
            pointTransfer.setMessage(msg);
            pointTransferRepository.save(pointTransfer);
            response.setStatus(FAILURE);
            response.setEndingBalance(user.getPoints());
            response.setAmount(pointTransfer.getAmount());
            response.setMessage(msg);
            return response;
        }

        // Set the exchange rate
        pointTransfer.setExchangeRate(PROGRAM_EXCHANGERATE_MAP.get(pointTransfer.getDestination()));

        // Set the starting balance
        pointTransfer.setStartingBalance(user.getPoints());

        // Update the balance after deducting points
        long newBalance = user.getPoints() - pointTransfer.getAmount();
        pointTransfer.setEndingBalance(newBalance);

        // This calls 3rd Party API with all the info they need for their system,
        // assumed at this point to be fully contained in PointTransfer object
        if (!transferToThirdParty(pointTransfer)) {
            // rollback changes
            // record failure with other useful info into pointtransferrepository,
            // so it can be seen by /history
            pointTransfer.setStatus(FAILURE.name());
            String msg = "failed: 3rd Party Transaction Failure {" + pointTransfer.getMessage() + "}";
            pointTransfer.setMessage(msg);
            pointTransferRepository.save(pointTransfer);
            response.setStatus(FAILURE);
            response.setEndingBalance(user.getPoints());
            response.setAmount(pointTransfer.getAmount());
            response.setMessage(msg);
            return response;
        }

        //  Update The status field and persist
        //  NOTE:  This transaction is assumed to succeed.  In production, contingencies would be
        //         made to check and rollback if necessary
        pointTransfer.setStatus(SUCCESS.name());
        pointTransferRepository.save(pointTransfer);

        //  Update the User Point balance and persist
        //  NOTE:  This transaction is assumed to succeed.  See above.
        user.setPoints(newBalance);
        userRepository.save(user);

        //  Build and return the Response (Successful)
        sb.setLength(0);
        sb.append("UserId \'")
                .append(userId)
                .append("\' transferred ")
                .append(pointTransfer.getAmount())
                .append(" points to program \'")
                .append(pointTransfer.getDestination())
                .append("\'");
        response = new FundingResponse(userId, sb.toString());
        response.setStatus(SUCCESS);
        response.setStartingBalance(pointTransfer.getStartingBalance());
        response.setAmount(pointTransfer.getAmount());
        response.setEndingBalance(pointTransfer.getEndingBalance());
        response.setDestination(pointTransfer.getDestination());
        return response;
    }

    /**
     *
     * @param userId - the id for which to return subscribed Rewards Programs
     * @return - Set<Program> of Rewards Programs the User is subscribed.
     *
     * NOTE:  This is a place-holder and will only return a non-null set for
     *        userId=1.  Furthermore, it returns the "test" set of programs.
     */
    private List<Program> getPrograms(long userId) {
        if (userId==1)
            return TEST_REWARDS_PROGRAMS;
        else
            return null;
    }

    /**
     * This is a placeholder that calls out to an echo http service rather than
     * an actual 3rd party API that receives the transfer points request and
     * (ostensibly) returning an informative response
     *
     * @param pointTransfer
     * @return - returns true if an echo ok() is received
     */
    private boolean transferToThirdParty(Pointtransfer pointTransfer) {
        // the "dummy" endpoint
        final String ECHO_URI = "http://localhost:8080/echo";

        // Send a GET to the uri and if it receives a Success code (200) then return true
        try {
            URL url = new URL(ECHO_URI);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            int status = con.getResponseCode();
            return (status==200);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // hopefully this is never triggered
        return false;
    }

    //  Resets the point count to 999 and clear the history
    public User reset(long id) {
        userRepository.updatePointsById(id,999);
        pointTransferRepository.removeAllById(1);

        return userRepository.findById(id);
    }
}
