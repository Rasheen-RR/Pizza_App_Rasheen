//package com.infinium.pizza_app_rasheen.data;
//
//import com.infinium.pizza_app_rasheen.data.model.LoggedInUser;
//
//import java.io.IOException;
//
//public class RegisterDataSource {
//
//    /**
//     * Class that handles authentication w/ login credentials and retrieves user information.
//     */
//
//        public Result<LoggedInUser> login(String username, String password) {
//
//            try {
//                // TODO: handle loggedInUser authentication
//                LoggedInUser fakeUser =
//                        new LoggedInUser(
//                                java.util.UUID.randomUUID().toString(),
//                                "Jane Doe");
//                return new Result.Success<>(fakeUser);
//            } catch (Exception e) {
//                return new Result.Error(new IOException("Error logging in", e));
//            }
//        }
//
//        public void logout() {
//            // TODO: revoke authentication
//        }
//}
