package com.example.ramin.passenger.Network;

import com.example.ramin.passenger.Model.DoTripDetailsModel;
import com.example.ramin.passenger.Model.PassengerFinancialAccountModel;
import com.example.ramin.passenger.Model.PassengerModel;
import com.example.ramin.passenger.Model.RegisteredTripsModel;
import com.example.ramin.passenger.Model.ReserveTripModel;
import com.example.ramin.passenger.Model.ResponseModel;
import com.example.ramin.passenger.Model.SearchTripsModel;
import com.example.ramin.passenger.Model.TransactionModel;
import com.example.ramin.passenger.Model.TripsModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface GetPassengerData {

    @POST("signUp.php")
    Call<PassengerModel> signUpPassenger(@Query("passenger_name") String pName,@Query("passenger_family") String pFamily,@Query("passenger_sexuality") String pSexuality,@Query("passenger_mail") String pMail,@Query("passenger_mobile") String pMobile,@Query("passenger_password") String pPassword);

    @GET("login.php")
    Call<PassengerModel> passengerLogin(@Query("passenger_mail") String pMail,@Query("passenger_password") String pPassword);

    @GET("searchTrip.php")
    Call<List<SearchTripsModel>> getSearchTrips(@Query("origin") String origin,@Query("destination") String destination,@Query("date_of_movement") String dateOfMovement);

    @GET("selectTripDetails.php")
    Call<List<ReserveTripModel>> getTripDetails(@Query("trip_id") int tripId, @Query("sub_id") int subId);

    @GET("insertReserveTrip.php")
    Call<ResponseModel> insertReserveTrip(@Query("trip_id") int tripId,@Query("passenger_id") int passengerId,@Query("sub_id") int subId,@Query("payment_type") String paymentType,@Query("chair_reserved_count") int chairCount,@Query("passenger_bar") String passengerBar,@Query("cost_of_payment") int cost);

    @GET("selectRegisteredTrip.php")
    Call<List<RegisteredTripsModel>> getRegisteredTrips(@Query("passenger_id") int passengerId);

    @GET("cancelRegisteredTrip.php")
    Call<ResponseModel> cancelRegisteredTrip(@Query("trip_id") int tripId,@Query("sub_id") int suId,@Query("passenger_id") int passengerId);

    @GET("selectInventory.php")
    Call<PassengerFinancialAccountModel> getInventory(@Query("passenger_id") int passengerId);

    @GET("increaseCredit.php")
    Call<ResponseModel> increaseCredit(@Query("money") int moneyIncrease,@Query("passenger_id") int passengerId,@Query("date") String dateIncrease,@Query("time") String timeIncrease);

    @GET("decreaseCredit.php")
    Call<ResponseModel> insertPayment(@Query("money") int moneyDecrease,@Query("passenger_id") int passengerId,@Query("date") String dateDecrease,@Query("time") String timeDecrease);

    @GET("selectTransaction.php")
    Call<List<TransactionModel>> getTransaction(@Query("passenger_id") int passengerId);

    @GET("updateProfile.php")
    Call<ResponseModel> updateProfile(@Query("passenger_id") int pId,@Query("passenger_name") String pName,@Query("passenger_family") String pFamily,@Query("passenger_sexuality") String pSexuality,@Query("passenger_mail") String pMail,@Query("passenger_mobile") String pMobile,@Query("passenger_password") String pPassword);

    @GET("selectDoTrips.php")
    Call<List<TripsModel>> getDoTrips(@Query("passenger_id") int pId);

    @GET("selectDoTripDetails.php")
    Call<List<DoTripDetailsModel>> getDoTripDetails(@Query("trip_id") int tripId,@Query("sub_id") int subId);

    @GET("selectAllTrips.php")
    Call<List<SearchTripsModel>> getAllTrips();

    @GET("emptyChairCount.php")
    Call<SearchTripsModel> getEmptyChairCount(@Query("trip_id") int tripId,@Query("sub_id") int subId);

     @GET("tripPath.php")
    Call<TripsModel> getTripPath(@Query("trip_id") int tripId);

     @GET("updateSubtripChairCount.php")
    Call<ResponseModel> updateSubtripEmptyChairCount (@Query("trip_id") int tripId,@Query("sub_id") int subId);
}
