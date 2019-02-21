package com.app.lastmile.database;

import android.util.Log;

import com.app.lastmile.ListItemDelivery;
import com.app.lastmile.MyApplication;
import com.app.lastmile.separate.AppConstants;
import com.app.lastmile.separate.AssignDriverItem;
import com.app.lastmile.separate.CommonCallback;
import com.app.lastmile.separate.TrackBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class DatabaseController {
    public static void getDeliveredDeliveries(final int transporterId, final CommonCallback<ListItemDelivery> callback) {
        Observable.fromCallable(new Callable<List<ListItemDelivery>>() {
            @Override
            public List<ListItemDelivery> call() {
                return AppDatabase.getInstance(MyApplication.getInstance()).getOrderWarehouseStatusDao().getDeliveredRecords(transporterId);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<ListItemDelivery>>() {
                    @Override
                    public void call(List<ListItemDelivery> objs) {
                        if (callback != null) {
                            callback.onListReceived(objs);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                    }
                });
    }

    public static void addDummyJobs() {
        if (!AppConstants.ADD_DUMMY_JOB_LIST) {
            return;
        }
        Observable.fromCallable(new Callable<List<Job>>() {
            @Override
            public List<Job> call() {
                return AppDatabase.getInstance(MyApplication.getInstance()).getJobDao().getAlLJobsWithoutFilter();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Job>>() {
                    @Override
                    public void call(List<Job> geoList) {
                        if (geoList == null || geoList.size() == 0) {
                            // addDummyJobs2();
                            DatabaseController.getAllJobs(new CommonCallback<Job>() {
                                @Override
                                public void onListReceived(List<Job> list) {
                                    list.size();
                                }
                            });
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                    }
                });
    }

//    private static void addDummyJobs2() {
//        getOrders(new CommonCallback<Order>() {
//            @Override
//            public void onListReceived(List<Order> orders) {
//                List<Job> jobs = new ArrayList<>();
//                for (int i = 0; i < orders.size(); i++) {
//                    long expectedDeliveryTime = new Date().getTime() + 86400 * 1000 * (i + 2);
//                    Job job = new Job();
//                    job.setJobStatusId(AppConstants.JobStatus.JOB_STATUS_PENDING);
//                    job.setOrderId(orders.get(i).getId());
//                    if (orders.get(i).getBuyerCity().toLowerCase().contains("ajmer")) {
//                        job.setUserId(2);
//                    } else {
//                        job.setUserId(3);
//                    }
//                    job.setScheduledEndTime(expectedDeliveryTime);
//                    job.setScheduledStartTime(expectedDeliveryTime - 4 * 60 * 60 * 1000/*4 hours*/);
//                    job.setOrderNo(orders.get(i).getOrderNo());
//                    job.setAddress(orders.get(i).getBuyerAddress());
//                    jobs.add(job);
//                }
//                addJobs(jobs, new CommonCallback<Void>() {
//                    @Override
//                    public void onListReceived(List<Void> list) {
//
//                    }
//                });
//            }
//        }, AppConstants.INTIAL_ORDER_STATUS_ID);
//    }

    public static void getAllJobs(final CommonCallback<Job> commonCallback) {
        Observable.fromCallable(new Callable<List<Job>>() {
            @Override
            public List<Job> call() {
                return AppDatabase.getInstance(MyApplication.getInstance()).getJobDao().getAlLJobsCity();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Job>>() {
                    @Override
                    public void call(List<Job> geoList) {
                        if (geoList == null || geoList.size() == 0) {
                            // addDummyJobs2();
                            if (commonCallback != null) {
                                commonCallback.onListReceived(geoList);
                            }
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                    }
                });

    }

    public static void checkLogin(final String username, final String password, final CommonCallback<User> callback) {
        Observable.fromCallable(new Callable<Long>() {
            @Override
            public Long call() {
                return AppDatabase.getInstance(MyApplication.getInstance()).getUserDao().getListCount();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long geoList) {
                        if (geoList == null || geoList == 0) {
                            insertUsers(username, password, callback);
                        } else {
                            checkLoginFromDatabase(username, password, callback);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                    }
                });
    }


    public static List<User> getUserList() {
        final List<User> userList = new ArrayList<>();

        //warehouse managers
        for (int i = 0; i < 14; i++) {
            User user = new User();
            switch (i) {
                case 0:
                    user.setFirstName("Chris");
                    user.setLastName("Pratt");
                    user.setDesignation(AppConstants.Designation.VP);
                    user.setAreaField(AppConstants.Area.INDIA);
                    break;
                case 1:
                    user.setFirstName("Karl");
                    user.setLastName("Albrecht");
                    user.setDesignation(AppConstants.Designation.REGIONAL_MANAGER);
                    user.setAreaField(AppConstants.Region.NORTH);
                    break;
                case 2:
                    user.setFirstName("Ingvar");
                    user.setLastName("Kamprad");
                    user.setDesignation(AppConstants.Designation.REGIONAL_MANAGER);
                    user.setAreaField(AppConstants.Region.SOUTH);
                    break;
                /*case 3:
                    user.setFirstName("John");
                    user.setLastName("Franklyn");
                    user.setDesignation(AppConstants.Designation.REGIONAL_MANAGER);
                    user.setAreaField(AppConstants.Region.CENTRAL);
                    break;*/
                case 4:
                    user.setFirstName("Leonard");
                    user.setLastName("Blavatnik");
                    user.setDesignation(AppConstants.Designation.REGIONAL_MANAGER);
                    user.setAreaField(AppConstants.Region.EAST);
                    break;
                case 5:
                    user.setFirstName("Stefan");
                    user.setLastName("Persson");
                    user.setDesignation(AppConstants.Designation.STATE_MANAGER);
                    user.setAreaField(AppConstants.Area.RAJASTHAN);
                    break;
                case 6:
                    user.setFirstName("Wang");
                    user.setLastName("Jianlin");
                    user.setDesignation(AppConstants.Designation.STATE_MANAGER);
                    user.setAreaField(AppConstants.Area.MADHYA_PRADESH);
                    break;
                case 7:
                    user.setFirstName("Aliko");
                    user.setLastName("Dangote");
                    user.setDesignation(AppConstants.Designation.STATE_MANAGER);
                    user.setAreaField(AppConstants.Area.KARNATAKA);
                    break;
                case 8:
                    user.setFirstName("George");
                    user.setLastName("Kaiser");
                    user.setDesignation(AppConstants.Designation.STATE_MANAGER);
                    user.setAreaField(AppConstants.Area.WEST_BENGAL);
                    break;
                case 9:
                    user.setFirstName("Andrew");
                    user.setLastName("Carnegie");
                    user.setDesignation(AppConstants.Designation.CITY_MANAGER);
                    user.setAreaField(AppConstants.Area.JAIPUR);
                    break;
                case 10:
                    user.setFirstName("Gord");
                    user.setLastName("Pettinger");
                    user.setDesignation(AppConstants.Designation.CITY_MANAGER);
                    user.setAreaField(AppConstants.Area.AJMER);
                    break;
                case 11:
                    user.setFirstName("Peter");
                    user.setLastName("Lee");
                    user.setDesignation(AppConstants.Designation.CITY_MANAGER);
                    user.setAreaField(AppConstants.Area.BHOPAL);
                    break;

                case 12:
                    user.setFirstName("Ken");
                    user.setLastName("Hodge");
                    user.setDesignation(AppConstants.Designation.CITY_MANAGER);
                    user.setAreaField(AppConstants.Area.BANGLORE);
                    break;
                case 13:
                    user.setFirstName("Liam");
                    user.setLastName("Krik");
                    user.setDesignation(AppConstants.Designation.CITY_MANAGER);
                    user.setAreaField(AppConstants.Area.KOLKATA);
                    break;


            }


            user.setTypeId(AppConstants.WAREHOUSE);
            user.setEmail("ware" + (i + 1) + "@q3.com");
            user.setPassword("123456");
            user.setCurrentCity("Gurgaon");
            user.setActive(true);
            userList.add(user);
        }
        //transporters
        for (int i = 0; i < 20; i++) {
            User u = new User();
            u.setDesignation("Worker");
            u.setTypeId(AppConstants.TRANSPORTER);
            u.setEmail("trans" + (i + 1) + "@q3.com");
            u.setPassword("123456");
            u.setCurrentCity("Jaipur");
            u.setVehicleId(AppConstants.vehicleList.get(i).getId());
            u.setVehicleNo(AppConstants.vehicleList.get(i).getVehicleNo());
            u.setActive(true);
            switch (i) {
                case 0:
                    u.setFirstName("Charlie");
                    u.setLastName("Day");
                    u.setTransporterName("Balaji Ltd.");
                    break;
                case 1:
                    u.setFirstName("Andy");
                    u.setLastName("Garcia");
                    u.setTransporterName("K.K.Sons");
                    break;
                case 2:
                    u.setFirstName("Michael");
                    u.setLastName("Sheen");
                    u.setTransporterName("Hariram Pvt. Ltd.");
                    break;
                case 3:
                    u.setFirstName("Michael");
                    u.setLastName("Sheen");
                    u.setTransporterName("Jaipur Golden Transport");
                    break;
                case 4:
                    u.setFirstName("Max");
                    u.setLastName("Martini");
                    u.setTransporterName("Pardesi Ltd.(C)");
                    u.setCourier(true);
                    break;
                case 5:
                    u.setFirstName("Idris");
                    u.setLastName("Alba");
                    u.setTransporterName("Hariram Pvt. Ltd.");
                    break;
                case 6:
                    u.setFirstName("Burn");
                    u.setLastName("Gorman");
                    u.setTransporterName("Bhanu Traders(C)");
                    u.setCourier(true);
                    break;
                case 7:
                    u.setFirstName("Cy");
                    u.setLastName("Young");
                    u.setTransporterName("Dev Transport Company");
                    break;
                case 8:
                    u.setFirstName("Brendan");
                    u.setLastName("Taylor");
                    u.setTransporterName("Hakika Transport Services Ltd");
                    break;
                case 9:
                    u.setFirstName("Craig");
                    u.setLastName("Ervine");
                    u.setTransporterName("Cube Movers Limited");
                    break;
                case 10:
                    u.setFirstName("Tendai");
                    u.setLastName("Chatara");
                    u.setTransporterName("Jihan Freighters Ltd");
                    break;
                case 11:
                    u.setFirstName("Sean");
                    u.setLastName("Williams");
                    u.setTransporterName("Roy Transmotors Ltd");
                    break;
                case 12:
                    u.setFirstName("Sarel");
                    u.setLastName("Burger");
                    u.setTransporterName("Panafrica Logistics(C)");
                    u.setCourier(true);
                    break;
                case 13:
                    u.setFirstName("Gerrie");
                    u.setLastName("Snyman");
                    u.setTransporterName("National Heavy Haulage");
                    break;
                case 14:
                    u.setFirstName("JJ");
                    u.setLastName("Smit");
                    u.setTransporterName("Pebbly Beach Transport");
                    break;
                case 15:
                    u.setFirstName("Ash");
                    u.setLastName("Ketchum");
                    u.setTransporterName("GKR Transport(C)");
                    u.setCourier(true);
                    break;
                case 16:
                    u.setFirstName("Brock");
                    u.setLastName("Absol");
                    u.setTransporterName("Marleys Transport(C)");
                    u.setCourier(true);
                    break;
                case 17:
                    u.setFirstName("Bryce");
                    u.setLastName("Harper");
                    u.setTransporterName("PBT Couriers(C)");
                    u.setCourier(true);
                    break;
                case 18:
                    u.setFirstName("Jackie");
                    u.setLastName("Robinson");
                    u.setTransporterName("NZ Express Transport");
                    break;
                case 19:
                    u.setFirstName("Mike");
                    u.setLastName("Trout");
                    u.setTransporterName("Allied Express Transport Brisbane");
                    break;

            }
            userList.add(u);
        }
        return userList;
    }

    private static void checkLoginFromDatabase(final String username, final String password, final CommonCallback<User> callback) {
        Observable.fromCallable(new Callable<List<User>>() {
            @Override
            public List<User> call() {
                return AppDatabase.getInstance(MyApplication.getInstance()).getUserDao().checkLogin(username, password);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<User>>() {
                    @Override
                    public void call(List<User> geoList) {
                        if (callback != null) {
                            if (geoList == null || geoList.size() == 0) {
                                callback.onListReceived(null);
                            } else {
                                callback.onListReceived(geoList);
                            }
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                    }
                });
    }

    public static void insertUsers(final String username, final String password, final CommonCallback<User> callback) {
        final List<User> userList = getUserList();

        Observable.fromCallable(new Callable<List<Long>>() {
            @Override
            public List<Long> call() {
                return AppDatabase.getInstance(MyApplication.getInstance()).getUserDao().insert(userList);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Long>>() {
                    @Override
                    public void call(List<Long> geoList) {
                        if (username != null && password != null) {
                            checkLoginFromDatabase(username, password, callback);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                    }
                });
    }

    public static void getTimeSlotList(final CommonCallback<AssignDriverItem> callback, final int statusId) {
        final List<AssignDriverItem> assignDriverItems = new ArrayList<>();
        getOrders(new CommonCallback<Order>() {
            @Override
            public void onListReceived(List<Order> list) {
                if (list != null) {
                    for (int i = 0; i < list.size(); i++) {
                        Order order = list.get(i);
                        assignDriverItems.add(new AssignDriverItem(order.getOrderNo(), new Date().getTime() + 86400 * 1000 * (i + 2), order.getBuyerCity()));
                    }
                    if (callback != null) {
                        callback.onListReceived(assignDriverItems);
                    }
                }
            }
        }, statusId);

    }

    public static void getOrderDetails(String qrCodeText, final CommonCallback<Order> callback) {
        getOrderDetailsRx(qrCodeText)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Order>>() {
                    @Override
                    public void call(List<Order> geoList) {
                        if (callback != null) {
                            callback.onListReceived(geoList);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (callback != null) {
                            callback.onListReceived(new ArrayList<Order>());
                        }
                    }
                });
    }

    public static void getOrderDetails(int orderId, final CommonCallback<Order> callback) {
        getOrderDetailsRx(orderId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Order>>() {
                    @Override
                    public void call(List<Order> geoList) {
                        if (callback != null) {
                            callback.onListReceived(geoList);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (callback != null) {
                            callback.onListReceived(new ArrayList<Order>());
                        }
                    }
                });
    }

    private static Observable<List<Order>> getOrderDetailsRx(final int orderId) {
        return Observable.fromCallable(new Callable<List<Order>>() {
            @Override
            public List<Order> call() {
                return AppDatabase.getInstance(MyApplication.getInstance()).getOrderDao().getOrder(orderId);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private static Observable<List<Order>> getOrderDetailsRx(final String qrCodeText) {
        return Observable.fromCallable(new Callable<List<Order>>() {
            @Override
            public List<Order> call() {
                return AppDatabase.getInstance(MyApplication.getInstance()).getOrderDao().getOrderByQRCode(qrCodeText);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static void getOrders(final CommonCallback<Order> callback, final int statusId, final User user) {
        getOrderStatusListRx()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<OrderStatus>>() {
                    @Override
                    public void call(List<OrderStatus> geoList) {
                        if (geoList == null || geoList.size() == 0) {
                            insertOrderStatusRx(callback, statusId, user);
                        } else {
                            checkAndAddOrderRecords(callback, statusId, user);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
    }

    public static void getOrders(final CommonCallback<Order> callback, final int statusId) {
        getOrders(callback, statusId, null);
    }

    private static void checkAndAddOrderRecords(final CommonCallback<Order> callback, final int statusId, final User user) {
        getOrderRecords(statusId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Order>>() {
                    @Override
                    public void call(List<Order> geoList) {
                        if (geoList == null || geoList.size() == 0) {
                            insertOrders(callback, statusId, user);
                        } else {
                            if (callback != null) {
                                List<Order> list = new ArrayList<>();
                                for (Order order : geoList) {
                                    //todo check assignment table and return orders accordingly
                                    if (order.getStatusId() == statusId && user != null && order.getBuyerCity().toLowerCase()
                                            .contains(user.getCurrentCity().toLowerCase())) {
                                        list.add(order);
                                    } else if (user == null && order.getStatusId() == statusId) {
                                        list.add(order);
                                    }
                                }
                                callback.onListReceived(list);
                            }
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
    }

    private static void insertOrders(final CommonCallback<Order> callback, final int statusId, final User user) {
        insertOrdersRx()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Long>>() {
                    @Override
                    public void call(List<Long> geoList) {
                        checkAndAddOrderRecords(callback, statusId, user);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
    }

    private static Observable<List<Long>> insertOrdersRx() {


        final Order orderBNGLR1 = new Order();
        orderBNGLR1.setOrderNo("ORD000BNGLR1");
        orderBNGLR1.setShipperName("Raj Electronics");
        orderBNGLR1.setShippingService("Standard");
        orderBNGLR1.setBuyerName("International Tech Park Bangalore");
        orderBNGLR1.setDescription("This is a sample order");
        orderBNGLR1.setOrderDateTime(new Date().getTime() - 86400 * 1000 * 13);
        orderBNGLR1.setBuyerAddress("International Tech Park Bangalore SEZ,, MLCP Road, Pattandur Agrahara, Whitefield, Pattandur Agrahara, Whitefield, Bengaluru, Karnataka 560066");
        orderBNGLR1.setSellerAddress("Ground Floor & Basement, SCO No. 18, Sector 10A, Khandsa Road, Gurugram, Haryana 122001");
        orderBNGLR1.setQrCodeText("q1");
        orderBNGLR1.setBuyerCity("Bengaluru");
        orderBNGLR1.setBuyerState("Karnataka");
        orderBNGLR1.setBuyerRegion(AppConstants.Region.SOUTH);
        orderBNGLR1.setSellerCity("Gurugram, Hariyana");
        orderBNGLR1.setContactNo("9876543210");
        orderBNGLR1.setOrderLocationLat(12.91061);
        orderBNGLR1.setOrderLocationLng(77.5743073);
        orderBNGLR1.setSellerLocationLat(24.5854);
        orderBNGLR1.setSellerLocationLng(73.7125);
        orderBNGLR1.setStatusId(AppConstants.INTIAL_ORDER_STATUS_ID);

        final Order orderBNGLR2 = new Order();
        orderBNGLR2.setOrderNo("ORD000BNGLR2");
        orderBNGLR2.setShipperName("Raj Electronics");
        orderBNGLR2.setShippingService("Standard");
        orderBNGLR2.setBuyerName("St. John's Medical College");
        orderBNGLR2.setDescription("This is a sample order");
        orderBNGLR2.setOrderDateTime(new Date().getTime() - 86400 * 1000 * 6);
        orderBNGLR2.setBuyerAddress("Sarjapur Main Road, John Nagar, Koramangala, Bengaluru, Karnataka 560034");
        orderBNGLR2.setSellerAddress("Ground Floor & Basement, SCO No. 18, Sector 10A, Khandsa Road, Gurugram, Haryana 122001");
        orderBNGLR2.setQrCodeText("q2");
        orderBNGLR2.setBuyerCity("Bengaluru");
        orderBNGLR2.setBuyerState("Karnataka");
        orderBNGLR2.setBuyerRegion(AppConstants.Region.SOUTH);
        orderBNGLR2.setSellerCity("Gurugram, Hariyana");
        orderBNGLR2.setContactNo("9876543210");
        orderBNGLR2.setOrderLocationLat(12.910926);
        orderBNGLR2.setOrderLocationLng(77.5743071);
        orderBNGLR2.setSellerLocationLat(24.5854);
        orderBNGLR2.setSellerLocationLng(73.7125);
        orderBNGLR2.setStatusId(AppConstants.INTIAL_ORDER_STATUS_ID);


        final Order orderBHO1 = new Order();
        orderBHO1.setOrderNo("ORD000BHO1");
        orderBHO1.setShipperName("Raj Electronics");
        orderBHO1.setShippingService("Standard");
        orderBHO1.setBuyerName("Aura Mall");
        orderBHO1.setDescription("This is a sample order");
        orderBHO1.setOrderDateTime(new Date().getTime() - 86400 * 1000 * 4);
        orderBHO1.setBuyerAddress("E-8, Trilanga Main Road, Arera Colony, Gulmohar, Bhopal, Madhya Pradesh 462039");
        orderBHO1.setSellerAddress("Ground Floor & Basement, SCO No. 18, Sector 10A, Khandsa Road, Gurugram, Haryana 122001");
        orderBHO1.setQrCodeText("q3");
        orderBHO1.setBuyerCity("Bhopal");
        orderBHO1.setBuyerState("Madhya Pradesh");
        orderBHO1.setBuyerRegion(AppConstants.Region.EAST);
        orderBHO1.setSellerCity("Gurugram, Hariyana");
        orderBHO1.setContactNo("9876543210");
        orderBHO1.setOrderLocationLat(23.1889619);
        orderBHO1.setOrderLocationLng(77.4318971);
        orderBHO1.setSellerLocationLat(24.5854);
        orderBHO1.setSellerLocationLng(73.7125);
        orderBHO1.setStatusId(AppConstants.INTIAL_ORDER_STATUS_ID);

        final Order orderKOL1 = new Order();
        orderKOL1.setOrderNo("ORD000KOL1");
        orderKOL1.setShipperName("Raj Electronics");
        orderKOL1.setShippingService("Standard");
        orderKOL1.setBuyerName("Dell Exclusive Store");
        orderKOL1.setDescription("This is a sample order");
        orderKOL1.setOrderDateTime(new Date().getTime() - 86400 * 1000 * 2);
        orderKOL1.setBuyerAddress("P- 47, CIT Road, BRS 10, Kankurgachi, Kankurgachi, Kolkata, West Bengal 700054");
        orderKOL1.setSellerAddress("Ground Floor & Basement, SCO No. 18, Sector 10A, Khandsa Road, Gurugram, Haryana 122001");
        orderKOL1.setQrCodeText("q4");
        orderKOL1.setBuyerCity("Kolkata");
        orderKOL1.setBuyerState("West Bengal");
        orderKOL1.setBuyerRegion(AppConstants.Region.EAST);
        orderKOL1.setSellerCity("Gurugram, Hariyana");
        orderKOL1.setContactNo("9876543210");
        orderKOL1.setOrderLocationLat(22.552690);
        orderKOL1.setOrderLocationLng(88.353260);
        orderKOL1.setSellerLocationLat(24.5854);
        orderKOL1.setSellerLocationLng(73.7125);
        orderKOL1.setStatusId(AppConstants.INTIAL_ORDER_STATUS_ID);

        final Order orderKOL2 = new Order();
        orderKOL2.setOrderNo("ORD000KOL2");
        orderKOL2.setShipperName("Raj Electronics");
        orderKOL2.setShippingService("Standard");
        orderKOL2.setBuyerName("Capital Electronics");
        orderKOL2.setDescription("This is a sample order");
        orderKOL2.setOrderDateTime(new Date().getTime() - 86400 * 1000 * 7);
        orderKOL2.setBuyerAddress("Shop No. P-161, Scheme - VIIM, V.I.P. Road, Ultadanga, Kolkata, West Bengal 700054");
        orderKOL2.setSellerAddress("Ground Floor & Basement, SCO No. 18, Sector 10A, Khandsa Road, Gurugram, Haryana 122001");
        orderKOL2.setQrCodeText("q5");
        orderKOL2.setBuyerCity("Kolkata");
        orderKOL2.setBuyerState("West Bengal");
        orderKOL2.setBuyerRegion(AppConstants.Region.EAST);

        orderKOL2.setSellerCity("Gurugram, Hariyana");
        orderKOL2.setContactNo("9876543210");
        orderKOL2.setOrderLocationLat(22.591102);
        orderKOL2.setOrderLocationLng(88.375292);
        orderKOL2.setSellerLocationLat(24.5854);
        orderKOL2.setSellerLocationLng(73.7125);
        orderKOL2.setStatusId(AppConstants.INTIAL_ORDER_STATUS_ID);

        final Order orderBKNR1 = new Order();
        orderBKNR1.setOrderNo("ORD000BKNR1");
        orderBKNR1.setShipperName("Jay Electronics");
        orderBKNR1.setShippingService("SuperFast Delivery");
        orderBKNR1.setBuyerName("Hero MotoCorp");
        orderBKNR1.setDescription("This is a sample order");
        orderBKNR1.setOrderDateTime(new Date().getTime() - 86400 * 1000 * 14);
        orderBKNR1.setBuyerAddress("Plot No 6, 13, GS Road, Near Goga Gate Circle, Harijan Basthi, Bikaner, Rajasthan 334001");
        orderBKNR1.setSellerAddress("Ground Floor & Basement, SCO No. 18, Sector 10A, Khandsa Road, Gurugram, Haryana 122001");
        orderBKNR1.setQrCodeText("q14");
        orderBKNR1.setBuyerCity("Bikaner");
        orderBKNR1.setBuyerState("Rajasthan");
        orderBKNR1.setBuyerRegion(AppConstants.Region.WEST);
        orderBKNR1.setSellerCity("Gurugram, Hariyana");
        orderBKNR1.setContactNo("9876543210");
        orderBKNR1.setOrderLocationLat(28.004813);
        orderBKNR1.setOrderLocationLng(73.3089993);
        orderBKNR1.setSellerLocationLat(24.5854);
        orderBKNR1.setSellerLocationLng(73.7125);
        orderBKNR1.setStatusId(AppConstants.INTIAL_ORDER_STATUS_ID);


        final Order orderKNPR1 = new Order();
        orderKNPR1.setOrderNo("ORD000KNPR1");
        orderKNPR1.setShipperName("Jay Electronics");
        orderKNPR1.setShippingService("SuperFast Delivery");
        orderKNPR1.setBuyerName("SS Automobile - Royal Enfield");
        orderKNPR1.setDescription("This is a sample order");
        orderKNPR1.setOrderDateTime(new Date().getTime() - 86400 * 1000 * 14);
        orderKNPR1.setBuyerAddress("W1/ 52, Saket Nagar, Kanpur, Uttar Pradesh 208006");
        orderKNPR1.setSellerAddress("Ground Floor & Basement, SCO No. 18, Sector 10A, Khandsa Road, Gurugram, Haryana 122001");
        orderKNPR1.setQrCodeText("q22");
        orderKNPR1.setBuyerCity("Kanpur");
        orderKNPR1.setBuyerState("Uttar Pradesh");
        orderKNPR1.setBuyerRegion(AppConstants.Region.NORTH);
        orderKNPR1.setSellerCity("Gurugram, Hariyana");
        orderKNPR1.setContactNo("9876543210");
        orderKNPR1.setOrderLocationLat(26.440841);
        orderKNPR1.setOrderLocationLng(80.3069493);
        orderKNPR1.setSellerLocationLat(24.5854);
        orderKNPR1.setSellerLocationLng(73.7125);
        orderKNPR1.setStatusId(AppConstants.INTIAL_ORDER_STATUS_ID);

        final Order orderKNPR2 = new Order();
        orderKNPR2.setOrderNo("ORD000KNPR2");
        orderKNPR2.setShipperName("Jay Electronics");
        orderKNPR2.setShippingService("SuperFast Delivery");
        orderKNPR2.setBuyerName("KTL Pvt Ltd, Ramadevi");
        orderKNPR2.setDescription("This is a sample order");
        orderKNPR2.setOrderDateTime(new Date().getTime() - 86400 * 1000 * 14);
        orderKNPR2.setBuyerAddress("B 292 Ramadevi G.T.Road, Lal Bungalow, Jajmau Sub Metro City, Kanpur, Uttar Pradesh 208001");
        orderKNPR2.setSellerAddress("Ground Floor & Basement, SCO No. 18, Sector 10A, Khandsa Road, Gurugram, Haryana 122001");
        orderKNPR2.setQrCodeText("q21");
        orderKNPR2.setBuyerCity("Kanpur");
        orderKNPR2.setBuyerState("Uttar Pradesh");
        orderKNPR2.setBuyerRegion(AppConstants.Region.NORTH);
        orderKNPR2.setSellerCity("Gurugram, Hariyana");
        orderKNPR2.setContactNo("9876543210");
        orderKNPR2.setOrderLocationLat(26.440841);
        orderKNPR2.setOrderLocationLng(80.3069493);
        orderKNPR2.setSellerLocationLat(24.5854);
        orderKNPR2.setSellerLocationLng(73.7125);
        orderKNPR2.setStatusId(AppConstants.INTIAL_ORDER_STATUS_ID);

        final Order orderBHL1 = new Order();
        orderBHL1.setOrderNo("ORD000BHL1");
        orderBHL1.setShipperName("Jay Electronics");
        orderBHL1.setShippingService("SuperFast Delivery");
        orderBHL1.setBuyerName("Jockey Exclusive Store");
        orderBHL1.setDescription("This is a sample order");
        orderBHL1.setOrderDateTime(new Date().getTime() - 86400 * 1000 * 14);
        orderBHL1.setBuyerAddress("58- Pech Area, Infront of Singhal Hospital, near Mahaveer Park, Sindhu Nagar, Shastri Nagar, Bhilwara, Rajasthan 311001");
        orderBHL1.setSellerAddress("Ground Floor & Basement, SCO No. 18, Sector 10A, Khandsa Road, Gurugram, Haryana 122001");
        orderBHL1.setQrCodeText("q13");
        orderBHL1.setBuyerCity("Bhilwara");
        orderBHL1.setBuyerState("Rajasthan");
        orderBHL1.setBuyerRegion(AppConstants.Region.WEST);
        orderBHL1.setSellerCity("Gurugram, Hariyana");
        orderBHL1.setContactNo("9876543210");
        orderBHL1.setOrderLocationLat(25.3511734);
        orderBHL1.setOrderLocationLng(74.6226001);
        orderBHL1.setSellerLocationLat(24.5854);
        orderBHL1.setSellerLocationLng(73.7125);
        orderBHL1.setStatusId(AppConstants.INTIAL_ORDER_STATUS_ID);

        final Order orderCNGH1 = new Order();
        orderCNGH1.setOrderNo("ORD000CNGH1");
        orderCNGH1.setShipperName("Jay Electronics");
        orderCNGH1.setShippingService("Standard");
        orderCNGH1.setBuyerName("Eureka Forbes Ltd");
        orderCNGH1.setDescription("This is a sample order");
        orderCNGH1.setOrderDateTime(new Date().getTime() - 86400 * 1000 * 14);
        orderCNGH1.setBuyerAddress("SCO 14(FF),, SECTOR 7C, MADHYA MARG, CHANDIGARH, Sector 7-C, Sector 7, Chandigarh, 160019");
        orderCNGH1.setSellerAddress("Ground Floor & Basement, SCO No. 18, Sector 10A, Khandsa Road, Gurugram, Haryana 122001");
        orderCNGH1.setQrCodeText("q15");
        orderCNGH1.setBuyerCity("Chandigarh");
        orderCNGH1.setBuyerState("Punjab");
        orderCNGH1.setBuyerRegion(AppConstants.Region.NORTH);
        orderCNGH1.setSellerCity("Gurugram, Hariyana");
        orderCNGH1.setContactNo("9876543210");
        orderCNGH1.setOrderLocationLat(30.7275047);
        orderCNGH1.setOrderLocationLng(76.7716002);
        orderCNGH1.setSellerLocationLat(24.5854);
        orderCNGH1.setSellerLocationLng(73.7125);
        orderCNGH1.setStatusId(AppConstants.INTIAL_ORDER_STATUS_ID);

        final Order orderAJ1 = new Order();
        orderAJ1.setOrderNo("ORD000AJ1");
        orderAJ1.setShipperName("Jay Electronics");
        orderAJ1.setShippingService("Standard");
        orderAJ1.setBuyerName("ExxonMobil");
        orderAJ1.setDescription("This is a sample order");
        orderAJ1.setOrderDateTime(new Date().getTime() - 86400 * 1000 * 14);
        orderAJ1.setBuyerAddress("111, Faiz Manzil, Nathwan Shah, Dargah Sharif, Khadim Mohalla, Ajmer, Rajasthan 305001");
        orderAJ1.setSellerAddress("Ground Floor & Basement, SCO No. 18, Sector 10A, Khandsa Road, Gurugram, Haryana 122001");
        orderAJ1.setQrCodeText("q6");
        orderAJ1.setBuyerCity("Ajmer");
        orderAJ1.setBuyerState("Rajasthan");
        orderAJ1.setBuyerRegion(AppConstants.Region.WEST);
        orderAJ1.setSellerCity("Gurugram, Hariyana");
        orderAJ1.setContactNo("9876543210");
        orderAJ1.setOrderLocationLat(26.456118);
        orderAJ1.setOrderLocationLng(74.6260124);
        orderAJ1.setSellerLocationLat(24.5854);
        orderAJ1.setSellerLocationLng(73.7125);
        orderAJ1.setStatusId(AppConstants.INTIAL_ORDER_STATUS_ID);

        final Order orderAJ2 = new Order();
        orderAJ2.setOrderNo("ORD000AJ2");
        orderAJ2.setShipperName("Jay Electronics");
        orderAJ2.setShippingService("Standard");
        orderAJ2.setBuyerName("McKesson Corporation");
        orderAJ2.setDescription("This is a sample order");
        orderAJ2.setOrderDateTime(new Date().getTime() - 86400 * 1000 * 12);
        orderAJ2.setBuyerAddress("SHIV BHAWAN, P.B. NO. 21, Ramnagar, Pushkar Rd, Dayanand Colony, Ajmer, Rajasthan 305004");
        orderAJ2.setSellerAddress("Ground Floor & Basement, SCO No. 18, Sector 10A, Khandsa Road, Gurugram, Haryana 122001");
        orderAJ2.setQrCodeText("q7");
        orderAJ2.setBuyerCity("Ajmer");
        orderAJ2.setBuyerState("Rajasthan");
        orderAJ2.setBuyerRegion(AppConstants.Region.WEST);
        orderAJ2.setSellerCity("Gurugram, Hariyana");
        orderAJ2.setOrderLocationLat(26.4654876);
        orderAJ2.setOrderLocationLng(74.616107);
        orderAJ2.setSellerLocationLat(24.5854);
        orderAJ2.setSellerLocationLng(73.7125);
        orderAJ2.setContactNo("9876543210");
        orderAJ2.setStatusId(AppConstants.INTIAL_ORDER_STATUS_ID);

        final Order orderJP1 = new Order();
        orderJP1.setOrderNo("ORD000JP1");
        orderJP1.setShipperName("Jay Electronics");
        orderJP1.setShippingService("Standard");
        orderJP1.setBuyerName("CVS Health");
        orderJP1.setDescription("This is a sample order");
        orderJP1.setOrderDateTime(new Date().getTime() - 86400 * 1000 * 9);
        orderJP1.setBuyerAddress("Sri Krishna Service Station Amer Road Inside Hpcl, Opp Jal Mahal & Man Sagar Lake, Parasrampuri, Jaipur, Rajasthan 302002");
        orderJP1.setSellerAddress("Ground Floor & Basement, SCO No. 18, Sector 10A, Khandsa Road, Gurugram, Haryana 122001");
        orderJP1.setQrCodeText("q8");
        orderJP1.setBuyerCity("Jaipur");
        orderJP1.setBuyerState("Rajasthan");
        orderJP1.setBuyerRegion(AppConstants.Region.WEST);
        orderJP1.setSellerCity("Gurugram, Hariyana");
        orderJP1.setOrderLocationLat(26.9535678);
        orderJP1.setOrderLocationLng(75.8437479);
        orderJP1.setSellerLocationLat(24.5854);
        orderJP1.setSellerLocationLng(73.7125);
        orderJP1.setContactNo("9876543210");
        orderJP1.setStatusId(AppConstants.INTIAL_ORDER_STATUS_ID);

        final Order orderJP2 = new Order();
        orderJP2.setOrderNo("ORD000JP2");
        orderJP2.setShipperName("Jay Electronics");
        orderJP2.setShippingService("Standard");
        orderJP2.setBuyerName("Berkshire Hathaway");
        orderJP2.setDescription("This is a sample order");
        orderJP2.setOrderDateTime(new Date().getTime() - 86400 * 1000 * 7);
        orderJP2.setBuyerAddress("Shop No.1, Nagar Nigam Bazar, Sector 1, Malviya Nagar, Jaipur, Rajasthan 302017");
        orderJP2.setSellerAddress("Ground Floor & Basement, SCO No. 18, Sector 10A, Khandsa Road, Gurugram, Haryana 122001");
        orderJP2.setQrCodeText("q9");
        orderJP2.setBuyerCity("Jaipur");
        orderJP2.setBuyerState("Rajasthan");
        orderJP2.setBuyerRegion(AppConstants.Region.WEST);
        orderJP2.setSellerCity("Gurugram, Hariyana");
        orderJP2.setOrderLocationLat(26.8498197);
        orderJP2.setOrderLocationLng(75.8196227);
        orderJP2.setSellerLocationLat(24.5854);
        orderJP2.setSellerLocationLng(73.7125);
        orderJP2.setContactNo("9876543210");
        orderJP2.setStatusId(AppConstants.INTIAL_ORDER_STATUS_ID);

        final Order orderJP3 = new Order();
        orderJP3.setOrderNo("ORD000JP3");
        orderJP3.setShipperName("Jay Electronics");
        orderJP3.setShippingService("Standard");
        orderJP3.setBuyerName("Raj Electronics");
        orderJP3.setDescription("This is a sample order");
        orderJP3.setOrderDateTime(new Date().getTime() - 86400 * 1000 * 15);
        orderJP3.setBuyerAddress("Amrapali Marg, Nemi Nagar, Vaishali Nagar, Jaipur, Rajasthan 302021");
        orderJP3.setSellerAddress("Ground Floor & Basement, SCO No. 18, Sector 10A, Khandsa Road, Gurugram, Haryana 122001");
        orderJP3.setQrCodeText("q10");
        orderJP3.setBuyerCity("Jaipur");
        orderJP3.setBuyerState("Rajasthan");
        orderJP3.setBuyerRegion(AppConstants.Region.WEST);
        orderJP3.setSellerCity("Gurugram, Hariyana");
        orderJP3.setOrderLocationLat(26.9109153);
        orderJP3.setOrderLocationLng(75.7407774);
        orderJP3.setSellerLocationLat(24.5854);
        orderJP3.setSellerLocationLng(73.7125);
        orderJP3.setContactNo("9876543210");
        orderJP3.setStatusId(AppConstants.INTIAL_ORDER_STATUS_ID);
        final Order orderCTTK1 = new Order();
        orderCTTK1.setOrderNo("ORD000CTTK1");
        orderCTTK1.setShipperName("Raj Electronics");
        orderCTTK1.setShippingService("Fast Delivery");
        orderCTTK1.setBuyerName("JMG Automobiles");
        orderCTTK1.setDescription("This is a sample order");
        orderCTTK1.setOrderDateTime(new Date().getTime() - 86400 * 1000 * 20);
        orderCTTK1.setBuyerAddress("Sahara, Bajrakabati Canal Rd, Old LIC Colony, Cuttack, Odisha 753012");
        orderCTTK1.setSellerAddress("Ground Floor & Basement, SCO No. 18, Sector 10A, Khandsa Road, Gurugram, Haryana 122001");
        orderCTTK1.setQrCodeText("q12");
        orderCTTK1.setBuyerCity("Cuttack");
        orderCTTK1.setBuyerState("Odisha");
        orderCTTK1.setBuyerRegion(AppConstants.Region.EAST);
        orderCTTK1.setSellerCity("Gurugram, Hariyana");
        orderCTTK1.setContactNo("9876543210");
        orderCTTK1.setOrderLocationLat(20.463791);
        orderCTTK1.setOrderLocationLng(85.8848594);
        orderCTTK1.setSellerLocationLat(24.5854);
        orderCTTK1.setSellerLocationLng(73.7125);
        orderCTTK1.setStatusId(AppConstants.INTIAL_ORDER_STATUS_ID);


        final Order orderRRKL1 = new Order();
        orderRRKL1.setOrderNo("ORD000RRKL1");
        orderRRKL1.setShipperName("Raj Electronics");
        orderRRKL1.setShippingService("Fast Delivery");
        orderRRKL1.setBuyerName("TVS Motors Service Center");
        orderRRKL1.setDescription("This is a sample order");
        orderRRKL1.setOrderDateTime(new Date().getTime() - 86400 * 1000 * 20);
        orderRRKL1.setBuyerAddress("Civil Twp, Rourkela, Odisha 769004");
        orderRRKL1.setSellerAddress("Ground Floor & Basement, SCO No. 18, Sector 10A, Khandsa Road, Gurugram, Haryana 122001");
        orderRRKL1.setQrCodeText("q11");
        orderRRKL1.setBuyerCity("Rourkela");
        orderRRKL1.setBuyerState("Odisha");
        orderRRKL1.setBuyerRegion(AppConstants.Region.EAST);
        orderRRKL1.setSellerCity("Gurugram, Hariyana");
        orderRRKL1.setContactNo("9876543210");
        orderRRKL1.setOrderLocationLat(22.2277604);
        orderRRKL1.setOrderLocationLng(84.8161912);
        orderRRKL1.setSellerLocationLat(24.5854);
        orderRRKL1.setSellerLocationLng(73.7125);
        orderRRKL1.setStatusId(AppConstants.INTIAL_ORDER_STATUS_ID);
        return Observable.fromCallable(new Callable<List<Long>>() {
            @Override
            public List<Long> call() {
                return AppDatabase.getInstance(MyApplication.getInstance()).getOrderDao().insert(
                        orderBNGLR1,
                        orderBNGLR2,
                        orderCTTK1,
                        orderRRKL1,

                        orderBHO1,
                        orderKOL1,
                        orderKOL2,
                        orderBKNR1,
                        orderKNPR1,
                        orderKNPR2,
                        orderBHL1,
                        orderCNGH1,
                        orderAJ1,
                        orderAJ2,
                        orderJP1,
                        orderJP2,
                        orderJP3);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private static Observable<List<Order>> getOrderRecords(final int statusId) {
        return Observable.fromCallable(new Callable<List<Order>>() {
            @Override
            public List<Order> call() {
                return AppDatabase.getInstance(MyApplication.getInstance()).getOrderDao().getOrders();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private static void insertOrderStatusRx(final CommonCallback<Order> callback, final int statusId, final User user) {
        insertOrderStatusRecords().subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Long>>() {
                    @Override
                    public void call(List<Long> geoList) {
                        getOrders(callback, statusId, user);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
    }

    private static Observable<List<Long>> insertOrderStatusRecords() {
        final List<OrderStatus> list = new ArrayList<>();
        list.add(new OrderStatus("New", "#FFFAAA"));
        list.add(new OrderStatus("Ready for Shipment", "#B3E5FC"));
        list.add(new OrderStatus("In Transit", "#FFA913"));
        list.add(new OrderStatus("Goods Arrived at Location", "#B1BEC6"));
        list.add(new OrderStatus("Goods Delivered", "#01B051"));

        return Observable.fromCallable(new Callable<List<Long>>() {
            @Override
            public List<Long> call() {
                return AppDatabase.getInstance(MyApplication.getInstance()).getOrderStatusDao().insert(list);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    private static Observable<List<OrderStatus>> getOrderStatusListRx() {
        return Observable.fromCallable(new Callable<List<OrderStatus>>() {
            @Override
            public List<OrderStatus> call() throws Exception {
                return AppDatabase.getInstance(MyApplication.getInstance()).getOrderStatusDao().getStatusList();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        //return Observable.just(AppDatabase.getInstance(MainActivity.this).getOrderStatusDao().getStatusList());
    }

    public static void getOrderStatus(CommonCallback<OrderStatus> orderStatusCommonCallback) {

    }

    private static Observable<List<User>> getTransportersRx() {
        return Observable.fromCallable(new Callable<List<User>>() {
            @Override
            public List<User> call() throws Exception {
                return AppDatabase.getInstance(MyApplication.getInstance()).getUserDao().getTransporters();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static void getTransporters(final CommonCallback<User> transporterCallback) {
        getVehiclesRx()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Vehicle>>() {
                    @Override
                    public void call(List<Vehicle> geoList) {
                        if (geoList == null || geoList.size() == 0) {
                            insertVehicles(transporterCallback);
                        } else {
                            checkAndAddTransportersRecords(geoList, transporterCallback);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
    }

    private static void checkAndAddTransportersRecords(final List<Vehicle> vehicleList, final CommonCallback<User> transporterCallback) {
        getTransportersRx().subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<User>>() {
                    @Override
                    public void call(List<User> list) {
                        if (list == null || list.size() == 0) {
                            //insertTransporters(vehicleList, transporterCallback);
                        } else {
                            if (transporterCallback != null) {
                                transporterCallback.onListReceived(list);
                            }
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
    }

    private static void insertTransporters(final List<Vehicle> vehicleList, final CommonCallback<Transporter> transporterCallback) {
        final List<Transporter> list = new ArrayList<>();
        list.add(new Transporter("Rajesh", "Kumar", "K.K.Sons", "", true, vehicleList.get(0).getId(), vehicleList.get(0).getVehicleNo()));
        list.add(new Transporter("Anil", "Kumar", "Balaji Ltd.", "", true, vehicleList.get(1).getId(), vehicleList.get(1).getVehicleNo()));
        list.add(new Transporter("Mukesh", "Kumar", "Hariram Pvt. Ltd.", "", true, vehicleList.get(1).getId(), vehicleList.get(1).getVehicleNo()));
        list.add(new Transporter("Dinesh", "Kumar", "Bhanu Traders", "", true, vehicleList.get(2).getId(), vehicleList.get(2).getVehicleNo()));
        list.add(new Transporter("Manish", "Kumar", "Pardesi Ltd.", "", true, vehicleList.get(3).getId(), vehicleList.get(3).getVehicleNo()));

        Observable.fromCallable(new Callable<List<Transporter>>() {
            @Override
            public List<Transporter> call() {
                AppDatabase.getInstance(MyApplication.getInstance()).getTransporterDao().insert(list);

                return list;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<List<Transporter>>() {
            @Override
            public void call(List<Transporter> geoList) {
                if (geoList != null || geoList.size() > 0) {
                    if (transporterCallback != null) {
                        transporterCallback.onListReceived(list);
                    }
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {

            }
        });

    }

    public static void getVehicles(final CommonCallback<Vehicle> callback) {
        getVehiclesRx().subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Vehicle>>() {
                    @Override
                    public void call(List<Vehicle> geoList) {
                        if (geoList == null || geoList.size() == 0) {
                            if (callback != null) {
                                callback.onListReceived(insertVehicles(null));
                            }

                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
    }

    private static List<Vehicle> insertVehicles(final CommonCallback<User> transporterCallback) {
        final List<Vehicle> list = new ArrayList<>();
        list.add(new Vehicle("Loading vehicle", 1000, "Heavy Vehicle", "RJ14 SS 4201"));
        list.add(new Vehicle("Loading vehicle", 845, "Average Vehicle", "RJ14 SS 4202"));
        list.add(new Vehicle("Loading vehicle", 1020, "HeavyDuty Vehicle", "RJ14 SS 4203"));
        list.add(new Vehicle("Loading vehicle", 200, "Light Vehicle", "RJ14 SS 4204"));
        list.add(new Vehicle("Loading vehicle", 350, "Light Vehicle", "RJ14 SS 4205"));

        Observable.fromCallable(new Callable<List<Vehicle>>() {
            @Override
            public List<Vehicle> call() {
                AppDatabase.getInstance(MyApplication.getInstance()).getVehicleDao().insert(list);
                return list;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Vehicle>>() {
                    @Override
                    public void call(List<Vehicle> geoList) {
                        if (geoList != null || geoList.size() > 0) {
                            checkAndAddTransportersRecords(geoList, transporterCallback);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
        return list;
    }

    public static void getJobsByUserAndStatus(final int userId, final int jobStatusId, final CommonCallback<Job> callback) {
        Observable.fromCallable(new Callable<List<Job>>() {
            @Override
            public List<Job> call() {
                return AppDatabase.getInstance(MyApplication.getInstance()).getJobDao().getJobsByUserAndStatus(userId, jobStatusId);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Job>>() {
                    @Override
                    public void call(List<Job> geoList) {
                        if (callback != null) {
                            callback.onListReceived(geoList);
                        }
                    }
                }, new Action1<Throwable>() {

                    @Override
                    public void call(Throwable throwable) {

                    }
                });
    }

    private static Observable<List<Vehicle>> getVehiclesRx() {
        return Observable.fromCallable(new Callable<List<Vehicle>>() {
            @Override
            public List<Vehicle> call() throws Exception {
                return AppDatabase.getInstance(MyApplication.getInstance()).getVehicleDao().getVehicles();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static void getPendingOrAcceptedJobs(final int userId, final CommonCallback<Job> callback) {
        getJobsByUserAndStatus(userId, AppConstants.JobStatus.JOB_STATUS_PENDING, new CommonCallback<Job>() {
            @Override
            public void onListReceived(List<Job> list) {
                if (list == null || list.size() == 0) {
                    getJobsByUserAndStatus(userId, AppConstants.JobStatus.JOB_STATUS_ACCEPTED, callback);
                } else {
                    if (callback != null) {
                        callback.onListReceived(list);
                    }
                }
            }
        });
    }

    public static void getPendingJobs(int userId, CommonCallback<Job> callback) {
        getJobsByUserAndStatus(userId, AppConstants.JobStatus.JOB_STATUS_PENDING, callback);
    }

    public static void getStartedOrAccceptedDeliveries(final int userId, final CommonCallback<ListItemDelivery> callback) {
        getStartedDeliveries(userId, new CommonCallback<ListItemDelivery>() {
            @Override
            public void onListReceived(List<ListItemDelivery> list) {
                if (list == null || list.size() == 0) {
                    getAcceptedNHoldDeliveries(userId, callback);
                } else {
                    callback.onListReceived(list);
                }
            }
        });
    }

    public static void getOrderDeliveriesList(final int userId, final CommonCallback<Order> callback) {
        Observable.fromCallable(new Callable<List<ListItemDelivery>>() {
            @Override
            public List<ListItemDelivery> call() {
                return AppDatabase.getInstance(MyApplication.getInstance()).getOrderWarehouseStatusDao().getOrderDeliveries(userId);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<ListItemDelivery>>() {
                    @Override
                    public void call(List<ListItemDelivery> objs) {
                        if (objs != null && objs.size() > 0) {
                            getDeliveryOrders(objs, callback);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                    }
                });
    }

    /* public static void getAccceptedJobs(final int userId, final CommonCallback<ListItemDelivery> callback) {
         getStartedDeliveries(userId, new CommonCallback<ListItemDelivery>() {
             @Override
             public void onListReceived(List<ListItemDelivery> list) {
                 if (list == null || list.size() == 0) {
                     getAcceptedDeliveries(userId, callback);
                 } else {
                     callback.onListReceived(list);
                 }
             }
         });
     }
 */
    private static void getStartedDeliveries(final int transporterId, final CommonCallback<ListItemDelivery> callback) {
        Observable.fromCallable(new Callable<List<ListItemDelivery>>() {
            @Override
            public List<ListItemDelivery> call() {
                return AppDatabase.getInstance(MyApplication.getInstance()).getOrderWarehouseStatusDao().getStarterDeliveries(transporterId);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<ListItemDelivery>>() {
                    @Override
                    public void call(List<ListItemDelivery> objs) {
                        if (callback != null) {
                            callback.onListReceived(objs);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                    }
                });
    }

    private static void getAcceptedNHoldDeliveries(final int transporterId, final CommonCallback<ListItemDelivery> callback) {
        Observable.fromCallable(new Callable<List<ListItemDelivery>>() {
            @Override
            public List<ListItemDelivery> call() {
                return AppDatabase.getInstance(MyApplication.getInstance()).getOrderWarehouseStatusDao().getAcceptedNHoldDeliveries(transporterId);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<ListItemDelivery>>() {
                    @Override
                    public void call(List<ListItemDelivery> objs) {
                        if (callback != null) {
                            callback.onListReceived(objs);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                    }
                });
    }

    private static void markDeliveriesEnd(final int transporterId, final CommonCallback<ListItemDelivery> callback) {
        Observable.fromCallable(new Callable<List<ListItemDelivery>>() {
            @Override
            public List<ListItemDelivery> call() {
                return AppDatabase.getInstance(MyApplication.getInstance()).getOrderWarehouseStatusDao().getAcceptedDeliveries(transporterId);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<ListItemDelivery>>() {
                    @Override
                    public void call(List<ListItemDelivery> objs) {
                        if (callback != null) {
                            callback.onListReceived(objs);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                    }
                });
    }

    public static void getOrderByOrderId(final String orderNo, final CommonCallback<Order> callback) {
        Observable.fromCallable(new Callable<List<Order>>() {
            @Override
            public List<Order> call() {
                return AppDatabase.getInstance(MyApplication.getInstance()).getOrderDao().getOrderByNo(orderNo);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Order>>() {
                    @Override
                    public void call(List<Order> geoList) {
                        if (callback != null) {
                            callback.onListReceived(geoList);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                    }
                });
    }

    public static void getOrdersByOrderId(final List<ListItemDelivery> orderList, final CommonCallback<Order> callback) {
        Observable.fromCallable(new Callable<List<Order>>() {
            @Override
            public List<Order> call() {
                List<Integer> orderIdList = new ArrayList<>();
                for (ListItemDelivery item : orderList) {
                    orderIdList.add(item.getId());
                }
                return AppDatabase.getInstance(MyApplication.getInstance()).getOrderDao().getOrders(orderIdList);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Order>>() {
                    @Override
                    public void call(List<Order> geoList) {
                        if (callback != null) {
                            if (geoList != null && geoList.size() > 0) {
                                callback.onListReceived(geoList);
                            }
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                    }
                });
    }

    public static void getEndedJobs(int userId, CommonCallback<Job> callback) {
        getJobsByUserAndStatus(userId, AppConstants.JobStatus.JOB_STATUS_ENDED, callback);
    }

    public static void markDeliveriesStarted(final List<Integer> orderIdList, final int transporterId,
                                             final List<Integer> deliveries,
                                             final CommonCallback<Void> callback) {
        Observable.fromCallable(new Callable<Void>() {
            @Override
            public Void call() {
                AppDatabase.getInstance(MyApplication.getInstance()).getOrderWarehouseStatusDao().updateStarted(deliveries);
                List<ListItemDelivery> list = AppDatabase.getInstance(MyApplication.getInstance()).getOrderWarehouseStatusDao().getStarterDeliveries(transporterId);
                List<OrderStatusChangeHistory> historyList = OrderStatusChangeHistory.getList(list);
                printHistoryList(historyList);
                AppDatabase.getInstance(MyApplication.getInstance()).getOrderStatusChangeHistoryDao()
                        .insert(historyList);

                AppDatabase.getInstance(MyApplication.getInstance()).getOrderDao().updateOrderStatus(orderIdList, Order.Status.IN_TRANSIT);

                return null;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void geoList) {
                        if (callback != null) {
                            callback.onListReceived(null);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                    }
                });
    }

    public static void markDeliveriesEnded(final User mineUserObj, final int transporterId, final List<ListItemDelivery> deliveries,
                                           final CommonCallback<Void> callback) {
        if (deliveries == null || deliveries.size() == 0) {
            return;
        }
        final AppDatabase appDatabase = AppDatabase.getInstance(MyApplication.getInstance());
        final OrderWarehouseStatusDao orderWarehouseStatusDao = appDatabase.getOrderWarehouseStatusDao();
        Observable.fromCallable(new Callable<Void>() {
            @Override
            public Void call() {
                int[] idArr = new int[deliveries.size()];
                for (int i = 0; i < idArr.length; i++) {
                    idArr[i] = deliveries.get(i).getId();
                }
                List<OrderStatusChangeHistory> list = new ArrayList<>();
                orderWarehouseStatusDao.updateEnded(idArr);
                for (OrderWarehouseStatus delivery : deliveries) {
                    OrderStatusChangeHistory obj = new OrderStatusChangeHistory(delivery, OrderWarehouseStatus.Status.DELIVERED);
                    list.add(obj);
                }
                printHistoryList(list);
                appDatabase.getOrderStatusChangeHistoryDao().insert(list);
                list.clear();
                for (int i = 0; i < deliveries.size(); i++) {
                    OrderWarehouseStatus orderWarehouseStatus = orderWarehouseStatusDao.getNextDelivery(deliveries.get(i).getWarehouseId(), deliveries.get(i).getOrderId());
                    if (orderWarehouseStatus == null) {

                    } else {
                        list.add(new OrderStatusChangeHistory(orderWarehouseStatus, OrderWarehouseStatus.Status.RECEIVE_CONFIRMATION_PENDING));
                        orderWarehouseStatusDao.updateNextDeliveryStatus(orderWarehouseStatus.getId());
                    }
                }

                printHistoryList(list);
                appDatabase.getOrderStatusChangeHistoryDao()
                        .insert(list);
                if (deliveries.size() == 1) {
                    //courier case
                    List<Integer> orderIdList = new ArrayList<>();
                    orderIdList.add(deliveries.get(0).getOrderId());
                    appDatabase.getOrderDao().updateOrderStatus(orderIdList, Order.Status.DELIVERED);
                }

                return null;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void geoList) {
                        if (callback != null) {
                            callback.onListReceived(null);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                    }
                });
    }

    private static void printHistoryList(List<OrderStatusChangeHistory> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        for (OrderStatusChangeHistory changeHistory : list) {
            Log.i("History", changeHistory.toString());
        }
    }

    public static void markDeliveriesEnded2(final List<OrderWarehouseStatus> statusList, final CommonCallback<Void> callback) {
        Observable.fromCallable(new Callable<Integer>() {
            @Override
            public Integer call() {
                List<OrderStatusChangeHistory> list = new ArrayList<>();
                List<Integer> idList = new ArrayList<>();
                List<Integer> widList = new ArrayList<>();
                for (OrderWarehouseStatus order : statusList) {
                    OrderStatusChangeHistory obj = new OrderStatusChangeHistory(order);
                    idList.add(order.getOrderId());
                    widList.add(order.getWarehouseId());
                    list.add(obj);
                }
                AppDatabase.getInstance(MyApplication.getInstance()).getOrderWarehouseStatusDao()
                        .updateStatus(idList, widList, OrderWarehouseStatus.Status.DELIVERED);
                printHistoryList(list);
                AppDatabase.getInstance(MyApplication.getInstance()).getOrderStatusChangeHistoryDao().insert(list);
                for (int i = 0; i < statusList.size(); i++) {
                    AppDatabase.getInstance(MyApplication.getInstance()).getOrderWarehouseStatusDao().updateNextDeliveryStatus(statusList.get(i).getWarehouseId(), statusList.get(i).getOrderId());
                }
                return 1;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer geoList) {

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
    }

    public static void markJobsAccepted(final int userId, final CommonCallback<Void> callback) {
        Observable.fromCallable(new Callable<Integer>() {
            @Override
            public Integer call() {
                return AppDatabase.getInstance(MyApplication.getInstance()).getJobDao().updateJobsStatus(userId, AppConstants.JobStatus.JOB_STATUS_PENDING, AppConstants.JobStatus.JOB_STATUS_ACCEPTED);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer geoList) {
                        if (callback != null) {
                            callback.onListReceived(geoList == null ? null : new ArrayList<Void>());
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (callback != null) {
                            callback.onListReceived(null);
                        }
                    }
                });
    }

    public static void markJobStarted(final List<Integer> jobIdList, final CommonCallback<Void> callback) {
        markJobStatus(jobIdList, AppConstants.JobStatus.JOB_STATUS_IN_PROGRESS, callback);
    }

    public static void markJobEnded(final List<Job> jobList, final List<Order> orderList, final String fromDesignation, final String imageFilePath,
                                    final String signatureFileName, final CommonCallback<Void> callback) {
        if (fromDesignation == null || jobList == null || jobList.size() == 0 || orderList == null || orderList.size() == 0) {
            return;
        }
        final List<Integer> jobIdList = new ArrayList<>();
        for (Job job : jobList) {
            jobIdList.add(job.getId());
        }
        final List<Integer> orderIdList = new ArrayList<>();
        for (Order order : orderList) {
            orderIdList.add(order.getId());
        }
        Observable.fromCallable(new Callable<Void>() {
            @Override
            public Void call() {
                AppDatabase.getInstance(MyApplication.getInstance()).getJobDao().updateJobStatusEnded(jobIdList, AppConstants.JobStatus.JOB_STATUS_ENDED, imageFilePath, signatureFileName);
                String locationReached = "";
                if (fromDesignation.equalsIgnoreCase(AppConstants.Designation.VP)) {
                    //get region from order and update to currentLocationReached
                    locationReached = orderList.get(0).getBuyerRegion();
                } else if (fromDesignation.equalsIgnoreCase(AppConstants.Designation.REGIONAL_MANAGER)) {
                    //get state from order and update to currentLocationReached
                    locationReached = orderList.get(0).getBuyerState();
                } else if (fromDesignation.equalsIgnoreCase(AppConstants.Designation.STATE_MANAGER)) {
                    //get city from order and update to currentLocationReached
                    locationReached = orderList.get(0).getBuyerCity();
                } else if (fromDesignation.equalsIgnoreCase(AppConstants.Designation.CITY_MANAGER)) {

                }
                AppDatabase.getInstance(MyApplication.getInstance()).getOrderDao().updateOrderCurrentLocation(orderIdList, locationReached);
                AppDatabase.getInstance(MyApplication.getInstance()).getOrderDao().updateStatusIds(orderIdList, AppConstants.OrderPendingStatus.ORDER_STATUS_PENDING_RECEIVED);
                return null;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void geoList) {
                        if (callback != null) {
                            callback.onListReceived(null);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
    }


    public static void markJobStatus(final List<Integer> jobIdList, final int newStatusId, final CommonCallback<Void> callback) {
        Observable.fromCallable(new Callable<Void>() {
            @Override
            public Void call() {
                AppDatabase.getInstance(MyApplication.getInstance()).getJobDao().updateJobStatus(jobIdList, newStatusId);
                return null;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void geoList) {
                        if (callback != null) {
                            callback.onListReceived(null);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
    }

    /**
     * multiple orders are assigned to a transporter to deliver stock to a particular warehouse by a warehouseManager
     *
     * @param orderList
     * @param transporter
     * @param warehouseManager
     * @param callback
     */
    public static void assignOrder(final List<Order> orderList, final User transporter,
                                   final User warehouseManager, final CommonCallback<Void> callback) {
        if (orderList == null || orderList.size() == 0 || transporter == null || warehouseManager == null || callback == null) {
            return;
        }
        final List<OrderWarehouseStatus> newModelList = new ArrayList<>();
        for (Order order : orderList) {
            OrderWarehouseStatus newModel = new OrderWarehouseStatus();
            newModel.setOrderId(order.getId());
            newModel.setDeliveryStatus(OrderWarehouseStatus.Status.ASSIGNED);
            newModel.setTransporterId(transporter.getId());
            newModel.setWarehouseId(1/*main warehouse id,then order region,state,city*/);
            newModel.setAssignedByUserId(warehouseManager.getId());
            newModelList.add(newModel);
            for (int i = 0; i < 3; i++) {
                newModel = new OrderWarehouseStatus();
                newModel.setOrderId(order.getId());
                newModel.setDeliveryStatus(OrderWarehouseStatus.Status.NOT_VALID_STATUS);
                newModel.setTransporterId(0);
                if (i == 0) {
                    newModel.setWarehouseId(getWarehouseId(AppConstants.Designation.REGIONAL_MANAGER));
                } else if (i == 1) {
                    newModel.setWarehouseId(getWarehouseId(AppConstants.Designation.STATE_MANAGER));
                } else if (i == 2) {
                    newModel.setWarehouseId(getWarehouseId(AppConstants.Designation.CITY_MANAGER));
                }
                newModelList.add(newModel);
            }
        }

        insertTimeSlot(newModelList, orderList, transporter, warehouseManager, callback);

    }

    private static void insertTimeSlot(final List<OrderWarehouseStatus> newModelList,
                                       final List<Order> orderList, final User transporter,
                                       final User warehouseManager, final CommonCallback<Void> callback) {
        Observable.fromCallable(new Callable<List<Long>>() {

            @Override
            public List<Long> call() {
                final List<Integer> orderIdList = new ArrayList<>();
                for (Order order : orderList) {
                    orderIdList.add(order.getId());
                }
                AppDatabase.getInstance(MyApplication.getInstance()).getOrderDao().updateStatusIds(orderIdList, Order.Status.ASSIGNED);

                return AppDatabase.getInstance(MyApplication.getInstance()).getOrderWarehouseStatusDao().insert(newModelList);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Long>>() {
                    @Override
                    public void call(List<Long> longs) {
                        addOrderStatusHistory(orderList, warehouseManager.getId(), 0,
                                transporter.getId(), Order.Status.ASSIGNED, callback);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
    }

    private static void addOrderStatusHistory(final List<Order> orderList,
                                              final int warehouseManagerId,
                                              final int deliveryId, final int transporterId,
                                              final int orderStatus, final CommonCallback<Void> callback) {
        Observable.fromCallable(new Callable<List<Long>>() {

            @Override
            public List<Long> call() {
                List<OrderStatusChangeHistory> list = new ArrayList<>();
                for (Order order : orderList) {
                    OrderStatusChangeHistory obj = new OrderStatusChangeHistory(order, orderStatus);
                    obj.setTransporterId(transporterId);
                    obj.setWarehouseManagerId(warehouseManagerId);
                    list.add(obj);
                }
                printHistoryList(list);
                return AppDatabase.getInstance(MyApplication.getInstance())
                        .getOrderStatusChangeHistoryDao().insert(list);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Long>>() {
                    @Override
                    public void call(List<Long> longs) {
                        callback.onListReceived(null);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
    }

    public static int getWarehouseId(String designation) {
        if (designation.equalsIgnoreCase(AppConstants.Designation.REGIONAL_MANAGER)) {
            //return 2;
            return 5;
        } else if (designation.equalsIgnoreCase(AppConstants.Designation.STATE_MANAGER)) {
            //return 3;
            return 6;
        } else if (designation.equalsIgnoreCase(AppConstants.Designation.CITY_MANAGER)) {
            //return 4;
            return 7;
        }
        return 1;
    }

    public static void assignOrderToTransporter(final List<Order> orders, final User transporter, final User warehouseManager, final CommonCallback<Void> callback) {
        final List<OrderAssignment> orderAssignments = new ArrayList<>();

        if (orders == null) {
            return;
        }
        for (Order order : orders) {
            orderAssignments.add(new OrderAssignment(order.getId(), transporter.getId(), warehouseManager.getDesignation(), new Date().getTime()));
        }

        Observable.fromCallable(new Callable<List<Long>>() {
            @Override
            public List<Long> call() throws Exception {
                return AppDatabase.getInstance(MyApplication.getInstance()).getOrderAssignmentDao().insert(orderAssignments);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Long>>() {
                    @Override
                    public void call(List<Long> geoList) {
                        addJobsAndTimeSlots(geoList, orderAssignments, orders, transporter, warehouseManager, callback);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
    }

    private static void addJobsAndTimeSlots(List<Long> geoList, List<OrderAssignment> orderAssignments, final List<Order> orders, User transporter, User warehouseManager, final CommonCallback<Void> callback) {
        final List<OrderAssignmentTimeSlots> orderAssignmentTimeSlots = new ArrayList<>();
        final List<Job> jobs = new ArrayList<>();
        if (geoList != null) {
            for (int i = 0; i < geoList.size(); i++) {
                long expectedDeliveryTime = new Date().getTime() + 86400 * 1000 * (i + 2);
                orderAssignmentTimeSlots.add(new OrderAssignmentTimeSlots(orderAssignments.get(i).getId(),
                        orders.get(i).getBuyerAddress(), orders.get(i).getOrderLocationLat(), orders.get(i).getOrderLocationLng(),
                        expectedDeliveryTime));

                Job job = new Job();
                job.setJobStatusId(AppConstants.JobStatus.JOB_STATUS_PENDING);
                job.setOrderId(orders.get(i).getId());
                job.setUserId(transporter.getId());
                job.setScheduledEndTime(expectedDeliveryTime);
                job.setScheduledStartTime(expectedDeliveryTime - 4 * 60 * 60 * 1000/*4 hours*/);
                job.setOrderNo(orders.get(i).getOrderNo());
                job.setAddress(orders.get(i).getBuyerAddress());
                job.setBuyerCity(orders.get(i).getBuyerCity());
                job.setFromPosition(warehouseManager.getDesignation());
                jobs.add(job);
            }

        }
        Observable.fromCallable(new Callable<List<Long>>() {
            @Override
            public List<Long> call() throws Exception {
                return AppDatabase.getInstance(MyApplication.getInstance()).getOrderAssignmentTimeSlotsDao()
                        .insert(orderAssignmentTimeSlots);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Long>>() {
                    @Override
                    public void call(List<Long> longs) {

                        updateOrderStatus(longs, orders, jobs, callback);

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
    }

    private static void updateOrderStatus(List<Long> longs, final List<Order> orders, final List<Job> jobs, final CommonCallback<Void> callback) {
        Observable.fromCallable(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                List<Integer> list = new ArrayList<>();
                if (orders != null) {
                    for (Order order : orders) {
                        list.add(order.getId());
                    }
                    AppDatabase.getInstance(MyApplication.getInstance()).getOrderDao().updateToReadyShipmentStatus(list);
                }
                return null;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void longs) {
                        addJobs(jobs, callback);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
    }

    public static void updateWarehouseOrderStatus(final User user, final int oldStatus, final int newStatus, final List<Order> orders, final CommonCallback<Void> callback) {
        Observable.fromCallable(new Callable<Long>() {
            @Override
            public Long call() {
                return AppDatabase.getInstance(MyApplication.getInstance()).getWarehouseDao().getWarehouseId(user.getId());
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long wId) {
                        if (wId != null && wId > 0) {
                            updateOrderByStatus(oldStatus, newStatus, wId, orders, callback);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
    }

    public static void checkOrderWarehouseStatus(final User user, final List<Order> orders, final CommonCallback<OrderWarehouseStatus> callback) {
        Observable.fromCallable(new Callable<OrderWarehouseStatus>() {
            @Override
            public OrderWarehouseStatus call() {
                return AppDatabase.getInstance(MyApplication.getInstance()).getOrderWarehouseStatusDao().getOrderStatusId(orders.get(0).getId(), user.getId());
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<OrderWarehouseStatus>() {
                    @Override
                    public void call(OrderWarehouseStatus orderStatus) {
                        if (orderStatus != null) {
                            callback.onListReceived(Collections.singletonList(orderStatus));
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
    }

    private static void updateOrderByStatus(final int oldStatus, final int newStatus, final long warehouseId, final List<Order> orders, final CommonCallback<Void> callback) {
        Observable.fromCallable(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                List<Integer> list = new ArrayList<>();
                if (orders != null) {
                    for (Order order : orders) {
                        list.add(order.getId());
                    }
                    AppDatabase.getInstance(MyApplication.getInstance()).getOrderWarehouseStatusDao().updateOrderStatus(oldStatus, newStatus, warehouseId, list);
                    List<ListItemDelivery> statusList = AppDatabase.getInstance(MyApplication.getInstance())
                            .getOrderWarehouseStatusDao().getRecordsByDeliveryStatus(newStatus, warehouseId, list);

                    List<OrderStatusChangeHistory> historyList = OrderStatusChangeHistory.getList(statusList);
                    printHistoryList(historyList);
                    AppDatabase.getInstance(MyApplication.getInstance()).getOrderStatusChangeHistoryDao().insert(historyList);
                }
                return null;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void longs) {
                        if (callback != null) {
                            callback.onListReceived(null);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
    }

    private static void addJobs(final List<Job> jobs, final CommonCallback<Void> callback) {
        Observable.fromCallable(new Callable<List<Job>>() {
            @Override
            public List<Job> call() {
                AppDatabase.getInstance(MyApplication.getInstance()).getJobDao().insert(jobs);
                return jobs;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Job>>() {
                    @Override
                    public void call(List<Job> geoList) {
                        if (callback != null) {
                            callback.onListReceived(null);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });

    }

    public static void getOrders(final CommonCallback<Order> callback, final List<Integer> orderIds) {
        if (orderIds == null || orderIds.size() == 0 || callback == null) {
            return;
        }
        Observable.fromCallable(new Callable<List<Order>>() {
            @Override
            public List<Order> call() {
                return AppDatabase.getInstance(MyApplication.getInstance()).getOrderDao().getOrders(orderIds);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Order>>() {
                    @Override
                    public void call(List<Order> geoList) {
                        if (callback != null) {
                            callback.onListReceived(geoList);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
    }

    public static void getOrdersByOrderIds(final CommonCallback<Order> callback, final List<Integer> orderIds) {
        if (orderIds == null || orderIds.size() == 0 || callback == null) {
            return;
        }
        Observable.fromCallable(new Callable<List<Order>>() {
            @Override
            public List<Order> call() {
                return AppDatabase.getInstance(MyApplication.getInstance()).getOrderDao().getOrders(orderIds);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Order>>() {
                    @Override
                    public void call(List<Order> geoList) {
                        if (callback != null) {
                            callback.onListReceived(geoList);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
    }

    public static void getOrders(final List<Job> jobList, final CommonCallback<Order> callback) {
        if (jobList == null || jobList.size() == 0 || callback == null) {
            return;
        }
        final List<Integer> orderIds = new ArrayList<>();
        for (Job job : jobList) {
            orderIds.add(job.getOrderId());
        }
        Observable.fromCallable(new Callable<List<Order>>() {
            @Override
            public List<Order> call() {
                return AppDatabase.getInstance(MyApplication.getInstance()).getOrderDao().getOrders(orderIds);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Order>>() {
                    @Override
                    public void call(List<Order> geoList) {
                        if (callback != null) {
                            callback.onListReceived(geoList);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
    }

    public static void getDeliveryOrders(final List<ListItemDelivery> jobList, final CommonCallback<Order> callback) {
        if (jobList == null || jobList.size() == 0 || callback == null) {
            return;
        }
        final List<Integer> orderIds = new ArrayList<>();
        for (ListItemDelivery job : jobList) {
            orderIds.add(job.getOrderId());
        }
        Observable.fromCallable(new Callable<List<Order>>() {
            @Override
            public List<Order> call() {
                return AppDatabase.getInstance(MyApplication.getInstance()).getOrderDao().getOrders(orderIds);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Order>>() {
                    @Override
                    public void call(List<Order> geoList) {
                        if (callback != null) {
                            callback.onListReceived(geoList);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
    }
    /*public static void getOrders(final List<Integer> orderIds, final CommonCallback<Order> callback) {
        Observable.fromCallable(new Callable<List<Order>>() {
            @Override
            public List<Order> call() {
                return AppDatabase.getInstance(MyApplication.getInstance()).getOrderDao().getOrders(orderIds);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Order>>() {
                    @Override
                    public void call(List<Order> geoList) {
                        if (callback != null) {
                            callback.onListReceived(geoList);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
    }*/

    public static List<TrackBean> getJobTrackData() {
        List<TrackBean> list = new ArrayList<>();

        list.add(new TrackBean("Jaipur", 1, 1, 2, "Delivered", new Date().getTime() - 1000 * 60 - 60 * 2));
        list.add(new TrackBean("Jaipur", 1, 1, 1, "Reached Courier facility", new Date().getTime() - 1000 * 60 - 60 * 8));
        list.add(new TrackBean("Jaipur", 1, 1, 0, "Reached Jaipur Hub", new Date().getTime() - 1000 * 60 - 60 * 10));
        list.add(new TrackBean("Gurgaon", 1, 1, 0, "Dispatched from seller", new Date().getTime() - 1000 * 60 - 60 * 18));
        list.add(new TrackBean("Gurgaon", 1, 1, 0, "Packed", new Date().getTime() - 1000 * 60 - 60 * 20));
        return list;
    }

    public static List<TrackBean> getScheduleJobData() {
        List<TrackBean> list = new ArrayList<>();
/*new Date().getTime() - 1000 * 60 - 60 * 20));
new Date().getTime() - 1000 * 60 - 60 * 18));
new Date().getTime() - 1000 * 60 - 60 * 10));
new Date().getTime() - 1000 * 60 - 60 * 8));
new Date().getTime() - 1000 * 60 - 60 * 2));*/
        list.add(new TrackBean("Gurgaon", 1, 1, 0, "Packing - Gurgaon", new Date().getTime() - (1000L * 60 * 58 * 22*3)));
        list.add(new TrackBean("South", 1, 1, 0, "South Hub - South", new Date().getTime() - 1000 * 60 * 59 * 23*2L));
        list.add(new TrackBean("Karnataka", 1, 1, 0, "Karnataka Hub - Karnataka", new Date().getTime() - 1000 * 60 * 57 * 24L));
        list.add(new TrackBean("Banglore", 1, 1, 1, "Courier facility - Banglore", new Date().getTime()));
        list.add(new TrackBean("Banglore", 1, 1, 2, "Destination - Banglore", new Date().getTime() + 1000 * 60 * 60 * 20));

        return list;
    }

    public static void getOrdersByRegion(final int orderStatusId, final String region, final boolean shouldCheckCurrentLocation, final CommonCallback<Order> callback) {
        Observable.fromCallable(new Callable<List<Order>>() {
            @Override
            public List<Order> call() {
                if (shouldCheckCurrentLocation) {
                    return AppDatabase.getInstance(MyApplication.getInstance()).getOrderDao().getOrdersByRegionCheckLocation(orderStatusId, region);
                } else {
                    return AppDatabase.getInstance(MyApplication.getInstance()).getOrderDao().getOrdersByRegion(region);
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Order>>() {
                    @Override
                    public void call(List<Order> geoList) {
                        if (callback != null) {
                            callback.onListReceived(geoList);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
    }

    public static void getOrdersByState(final String state, final boolean shouldCheckCurrentLocation, final CommonCallback<Order> callback) {
        Observable.fromCallable(new Callable<List<Order>>() {
            @Override
            public List<Order> call() {
                if (shouldCheckCurrentLocation) {
                    return AppDatabase.getInstance(MyApplication.getInstance()).getOrderDao().getOrdersByStateCheckLocation(state);
                } else {
                    return AppDatabase.getInstance(MyApplication.getInstance()).getOrderDao().getOrdersByState(state);
                }

            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Order>>() {
                    @Override
                    public void call(List<Order> geoList) {
                        if (callback != null) {
                            callback.onListReceived(geoList);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
    }

    public static void getOrdersByCity(final String city, final boolean shouldCheckCurrentLocation, final CommonCallback<Order> callback) {
        Observable.fromCallable(new Callable<List<Order>>() {
            @Override
            public List<Order> call() {
                if (shouldCheckCurrentLocation) {
                    return AppDatabase.getInstance(MyApplication.getInstance()).getOrderDao().getOrdersByCityCheckLocation(city);
                } else {
                    return AppDatabase.getInstance(MyApplication.getInstance()).getOrderDao().getOrdersByCity(city);
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Order>>() {
                    @Override
                    public void call(List<Order> geoList) {
                        if (callback != null) {
                            callback.onListReceived(geoList);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
    }

    public static List<String> getRegions() {
        List<String> list = new ArrayList<>();
        list.add(AppConstants.Region.NORTH);
        list.add(AppConstants.Region.SOUTH);
        list.add(AppConstants.Region.EAST);
        list.add(AppConstants.Region.WEST);
        return list;
    }

    public static void getOrdersByArea(String selectedAreaField, String selectedAreaQuery,
                                       CommonCallback<Order> callback) {
        if (selectedAreaField.equalsIgnoreCase(AppConstants.AreaType.REGION)) {
            getOrdersByRegion(1, selectedAreaQuery, false, callback);
        } else if (selectedAreaField.equalsIgnoreCase(AppConstants.AreaType.STATE)) {
            getOrdersByState(selectedAreaQuery, false, callback);
        } else if (selectedAreaField.equalsIgnoreCase(AppConstants.AreaType.CITY)) {
            getOrdersByCity(selectedAreaQuery, false, callback);
        }
    }

    public static void getOrdersByType(String areaType, CommonCallback<Order> callback, int i) {
        getOrdersGroupByType(areaType, callback);
    }


    public static void getOrdersGroupByType(final String areaType, final CommonCallback<Order> callback) {
        Observable.fromCallable(new Callable<List<Order>>() {
            @Override
            public List<Order> call() {
                if (areaType.equalsIgnoreCase(AppConstants.AreaType.REGION)) {
                    return AppDatabase.getInstance(MyApplication.getInstance()).getOrderDao().getOrdersGroupByRegion();
                } else if (areaType.equalsIgnoreCase(AppConstants.AreaType.STATE)) {
                    return AppDatabase.getInstance(MyApplication.getInstance()).getOrderDao().getOrdersGroupByState();
                } else if (areaType.equalsIgnoreCase(AppConstants.AreaType.CITY)) {
                    return AppDatabase.getInstance(MyApplication.getInstance()).getOrderDao().getOrdersGroupByCity();
                }
                return AppDatabase.getInstance(MyApplication.getInstance()).getOrderDao().getOrdersGroupByCity();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Order>>() {
                    @Override
                    public void call(List<Order> geoList) {
                        if (callback != null) {
                            callback.onListReceived(geoList);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
    }

    public static ArrayList<Warehouse> getWarehousesObjs() {
        ArrayList<Warehouse> warehouseList = new ArrayList<>();

        Warehouse warehouse1 = new Warehouse();
        warehouse1.setUserId(1);
        warehouse1.setName("India Warehouse");
        warehouse1.setLatitude(32.7090179);
        warehouse1.setLongitude(74.8748692);
        warehouse1.setAddress("Plot-54, First floor, near SRTC Yard,, Transport Nagar, Narwal, Jammu and Kashmir 180006");
        warehouse1.setNumber(9876543210L);
        warehouseList.add(warehouse1);

        Warehouse warehouse2 = new Warehouse();
        warehouse2.setUserId(2);
        warehouse2.setName("North Warehouse");
        warehouse2.setLatitude(30.8908227);
        warehouse2.setLongitude(75.8391252);
        warehouse2.setAddress("Dalip Singh Market, Sangeet Cinema, Partap Chowk, Industrial Area-B, Ludhiana, Punjab 141003");
        warehouse2.setNumber(9876543210L);
        warehouseList.add(warehouse2);

        Warehouse warehouse3 = new Warehouse();
        warehouse3.setUserId(6);
        warehouse3.setName("Rajasthan Warehouse");
        warehouse3.setLatitude(28.0216219);
        warehouse3.setLongitude(73.2845209);
        warehouse3.setAddress("Municipal Rd, Mehron Ka Bas, Bikaner, Rajasthan 334001");
        warehouse3.setNumber(9876543210L);
        warehouseList.add(warehouse3);

        Warehouse warehouse4 = new Warehouse();
        warehouse4.setUserId(11);
        warehouse4.setName("Ajmer Warehouse");
        warehouse4.setLatitude(26.4486874);
        warehouse4.setLongitude(74.6335015);
        warehouse4.setAddress("Bhagwan Adinath Marg, Kaiser Ganj Rd, Parao, Ajmer, Rajasthan 305001");
        warehouse4.setNumber(9876543210L);
        warehouseList.add(warehouse4);

        Warehouse warehouse5 = new Warehouse();
        warehouse5.setUserId(3);
        warehouse5.setName("South Warehouse");
        warehouse5.setLatitude(20.5378328);
        warehouse5.setLongitude(85.9501729);
        warehouse5.setAddress("No 8, Chhatrabazar, Mahtab road,, Bajrakabati, Cuttack, Odisha 753012");
        warehouse5.setNumber(9876543210L);
        warehouseList.add(warehouse5);

        Warehouse warehouse6 = new Warehouse();
        warehouse6.setUserId(8);
        warehouse6.setName("Karnataka Warehouse");
        warehouse6.setLatitude(14.8358216);
        warehouse6.setLongitude(73.7892612);
        warehouse6.setAddress("Shop No. 20/2, Fazal-E-Salar Complex, 2nd Main Rd, Kalasipalyam New Extension, Kalasipalya, Bengaluru, Karnataka 560002");
        warehouse3.setNumber(9876543210L);
        warehouseList.add(warehouse6);

        Warehouse warehouse7 = new Warehouse();
        warehouse7.setUserId(13);
        warehouse7.setName("Bengaluru Warehouse");
        warehouse7.setLatitude(12.9390835);
        warehouse7.setLongitude(77.4785649);
        warehouse7.setAddress("Narasimha Layout, Yeshwanthpur, Bengaluru, Karnataka 560022");
        warehouse7.setNumber(9876543210L);
        warehouseList.add(warehouse7);

        return warehouseList;
    }

    public static void getWarehouses(final CommonCallback<Warehouse> callback) {
        Observable.fromCallable(new Callable<List<Warehouse>>() {
            @Override
            public List<Warehouse> call() {
                return AppDatabase.getInstance(MyApplication.getInstance()).getWarehouseDao().getAllWarehouses();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Warehouse>>() {
                    @Override
                    public void call(List<Warehouse> warehouseList) {
                        if (warehouseList == null || warehouseList.size() == 0) {
                            insertWarehouses(callback);
                        } else {
                            if (callback != null) {
                                callback.onListReceived(warehouseList);
                            }
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                    }
                });
    }

    public static void insertWarehouses(final CommonCallback<Warehouse> callback) {
        Observable.fromCallable(new Callable<List<Long>>() {
            @Override
            public List<Long> call() {
                return AppDatabase.getInstance(MyApplication.getInstance()).getWarehouseDao().insert(getWarehousesObjs());
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Long>>() {
                    @Override
                    public void call(List<Long> warehouseList) {
                        if (callback != null) {
                            callback.onListReceived(getWarehousesObjs());
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                    }
                });
    }

    public static void getOrderByStatusId(final User user, final int deliveryStatusId, final CommonCallback<Order> callback) {
        Observable.fromCallable(new Callable<Long>() {
            @Override
            public Long call() {
                return AppDatabase.getInstance(MyApplication.getInstance()).getWarehouseDao().getWarehouseId(user.getId());
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long wId) {
                        if (wId != null && wId > 0) {
                            getOrdersIdsByWarehouseId(wId, deliveryStatusId, callback);
                        } else {
                            callback.onListReceived(null);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
    }

    private static void getOrdersIdsByWarehouseId(final long wId, final int deliveryStatusId, final CommonCallback<Order> callback) {
        Observable.fromCallable(new Callable<List<Long>>() {
            @Override
            public List<Long> call() {
                return AppDatabase.getInstance(MyApplication.getInstance()).getOrderWarehouseStatusDao().getOrdersIdByStatus(deliveryStatusId, wId);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Long>>() {
                    @Override
                    public void call(List<Long> geoList) {
                        if (geoList != null && geoList.size() > 0) {
                            getOrdersByWarehouseId(geoList, callback);
                        } else {
                            callback.onListReceived(null);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
    }

    private static void getOrdersByWarehouseId(final List<Long> geoList, final CommonCallback<Order> callback) {
        Observable.fromCallable(new Callable<List<Order>>() {
            @Override
            public List<Order> call() {
                return AppDatabase.getInstance(MyApplication.getInstance()).getOrderDao().getRegionOrdersById(geoList);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Order>>() {
                    @Override
                    public void call(List<Order> geoList) {
                        if (callback != null) {
                            callback.onListReceived(geoList);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
    }

    public static void getPendingOrAcceptedOrHoldDeliveries(final int transporterId, final CommonCallback<ListItemDelivery> callback) {
        Observable.fromCallable(new Callable<List<ListItemDelivery>>() {
            @Override
            public List<ListItemDelivery> call() {
                return AppDatabase.getInstance(MyApplication.getInstance()).getOrderWarehouseStatusDao().getRecords(transporterId);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<ListItemDelivery>>() {
                    @Override
                    public void call(List<ListItemDelivery> objs) {
                        if (callback != null) {
                            callback.onListReceived(objs);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                    }
                });
    }

    public static void getWarehouses(final List<Integer> wIdList, final CommonCallback<Warehouse> callback) {
        Observable.fromCallable(new Callable<List<Warehouse>>() {
            @Override
            public List<Warehouse> call() {
                return AppDatabase.getInstance(MyApplication.getInstance()).getWarehouseDao().getWarehouses(wIdList);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Warehouse>>() {
                    @Override
                    public void call(List<Warehouse> warehouseList) {
                        if (callback != null) {
                            callback.onListReceived(warehouseList);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                    }
                });
    }

    public static void getWarehouseFromTransporter(final int transporterId, final int orderId, final CommonCallback<Warehouse> callback) {
        Observable.fromCallable(new Callable<List<OrderWarehouseStatus>>() {
            @Override
            public List<OrderWarehouseStatus> call() {
                return AppDatabase.getInstance(MyApplication.getInstance()).getOrderWarehouseStatusDao().getWarehouseOrderStatus(transporterId, orderId);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<OrderWarehouseStatus>>() {
                    @Override
                    public void call(List<OrderWarehouseStatus> wList) {
                        if (wList != null && wList.size() > 0) {
                            getWarehouseStatus(wList.get(0).getWarehouseId(), callback);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                    }
                });
    }

    public static void getWarehouseStatus(final int orderWarehouseId, final CommonCallback<Warehouse> callback) {
        Observable.fromCallable(new Callable<List<Warehouse>>() {
            @Override
            public List<Warehouse> call() {
                return AppDatabase.getInstance(MyApplication.getInstance()).getWarehouseDao().getWarehouses(Collections.singletonList(orderWarehouseId));
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Warehouse>>() {
                    @Override
                    public void call(List<Warehouse> warehouseList) {
                        if (callback != null) {
                            callback.onListReceived(warehouseList);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                    }
                });
    }

    public static void markDeliveriesAccepted(final int transporterId, final CommonCallback<Void> callback) {
        Observable.fromCallable(new Callable<Void>() {
            @Override
            public Void call() {
                AppDatabase.getInstance(MyApplication.getInstance()).getOrderWarehouseStatusDao().updateAccepted(transporterId);
                List<ListItemDelivery> statuses = AppDatabase.getInstance(MyApplication.getInstance()).getOrderWarehouseStatusDao().getAcceptedDeliveries(transporterId);
                List<OrderStatusChangeHistory> list = OrderStatusChangeHistory.getList(statuses);
                printHistoryList(list);
                AppDatabase.getInstance(MyApplication.getInstance()).getOrderStatusChangeHistoryDao().insert(list);
                return null;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void objs) {
                        if (callback != null) {
                            callback.onListReceived(null);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                    }
                });
    }

    public static void assignTransporterToExistingDelivery(final int assignedByUserId, final List<Order> list, final int warehouseId, final User transporter, final CommonCallback<Void> callback) {
        Observable.fromCallable(new Callable<Void>() {
            @Override
            public Void call() {
                if (list == null || list.size() == 0) {
                    return null;
                }
                List<Integer> orderIds = new ArrayList<>(list.size());
                for (Order order : list) {
                    orderIds.add(order.getId());
                }
                List<ListItemDelivery> deliveryList = AppDatabase
                        .getInstance(MyApplication.getInstance()).getOrderWarehouseStatusDao()
                        .getRecords(warehouseId, orderIds);
                List<Integer> idList = new ArrayList<>();
                for (ListItemDelivery delivery : deliveryList) {
                    idList.add(delivery.getId());
                }
                AppDatabase.getInstance(MyApplication.getInstance()).getOrderWarehouseStatusDao()
                        .updateAssigned(idList, OrderWarehouseStatus.Status.ASSIGNED, transporter.getId(), assignedByUserId
                        );
                List<OrderStatusChangeHistory> list = OrderStatusChangeHistory.getList(deliveryList);
                printHistoryList(list);
                AppDatabase.getInstance(MyApplication.getInstance()).getOrderStatusChangeHistoryDao().insert(list);
                return null;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void objs) {
                        if (callback != null) {
                            callback.onListReceived(null);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                    }
                });
    }

    public static void getAllHistory(final CommonCallback<OrderStatusChangeHistory> callback) {
        if (callback == null) {
            return;
        }
        Observable.fromCallable(new Callable<List<OrderStatusChangeHistory>>() {
            @Override
            public List<OrderStatusChangeHistory> call() {
                return AppDatabase.getInstance(MyApplication.getInstance()).getOrderStatusChangeHistoryDao().getAllRecords();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<OrderStatusChangeHistory>>() {
                    @Override
                    public void call(List<OrderStatusChangeHistory> list) {
                        if (callback != null) {
                            callback.onListReceived(list);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                    }
                });
    }

    public static void getOrderList(final CommonCallback<Order> callback) {
        Observable.fromCallable(new Callable<List<Order>>() {
            @Override
            public List<Order> call() {
                return AppDatabase.getInstance(MyApplication.getInstance()).getOrderDao().getOrders();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Order>>() {
                    @Override
                    public void call(List<Order> orderList) {
                        if (callback != null) {
                            callback.onListReceived(orderList);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                    }
                });
    }

    public static void isAssignedToCourierWala(final int orderId, final CommonCallback<User> callback) {
        Observable.fromCallable(new Callable<List<User>>() {
            @Override
            public List<User> call() {
                List<OrderWarehouseStatus> list = AppDatabase.getInstance(MyApplication.getInstance()).getOrderWarehouseStatusDao().getRecordsByOrderId(orderId);
                List<Integer> transporterIds = new ArrayList<>();
                for (OrderWarehouseStatus orderWarehouseStatus : list) {
                    transporterIds.add(orderWarehouseStatus.getTransporterId());
                }
                List<User> userList = AppDatabase.getInstance(MyApplication.getInstance()).getUserDao().getRecords(transporterIds);
                return userList;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<User>>() {
                    @Override
                    public void call(List<User> userList) {
                        if (callback != null) {
                            callback.onListReceived(userList);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (callback != null) {
                            callback.onListReceived(new ArrayList<User>());
                        }
                    }
                });
    }

    public static void getAllOrders() {
        getOrderStatusListRx()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<OrderStatus>>() {
                    @Override
                    public void call(List<OrderStatus> geoList) {
                        if (geoList == null || geoList.size() == 0) {
                            insertOrderStatusRx(null, 0, null);
                        } else {
                            checkAndAddOrderRecords(null, 0, null);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
    }

    public static void markJobHold(final int deliveryId, final CommonCallback<Void> callback) {
        if (callback == null) {
            return;
        }
        Observable.fromCallable(new Callable<Void>() {
            @Override
            public Void call() {
                AppDatabase.getInstance(MyApplication.getInstance()).getOrderWarehouseStatusDao().updateJobHold(deliveryId);
                return null;
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (callback != null) {
                            callback.onListReceived(null);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (callback != null) {
                            callback.onListReceived(null);
                        }
                    }
                });
    }

    public static void getDeliveries(final List<Integer> orderIds, final int transporterId, final CommonCallback<ListItemDelivery> commonCallback) {
        if (orderIds == null || orderIds.size() == 0 || commonCallback == null) {
            return;
        }
        Observable.fromCallable(new Callable<List<ListItemDelivery>>() {
            public List<ListItemDelivery> call() {
                return AppDatabase.getInstance(MyApplication.getInstance()).getOrderWarehouseStatusDao()
                        .getRecordsByOrderIdsAndTransporterId(orderIds, transporterId);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<ListItemDelivery>>() {
                    @Override
                    public void call(List<ListItemDelivery> deliveries) {
                        if (commonCallback != null) {
                            commonCallback.onListReceived(deliveries);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (commonCallback != null) {
                            commonCallback.onListReceived(new ArrayList<ListItemDelivery>());
                        }
                    }
                });

    }


}
