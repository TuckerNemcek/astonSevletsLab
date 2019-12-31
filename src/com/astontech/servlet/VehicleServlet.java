package com.astontech.servlet;

import com.astontech.bo.Vehicle;
import com.astontech.bo.VehicleMake;
import com.astontech.bo.VehicleModel;
import com.astontech.dao.mysql.*;
import common.helpers.DateHelper;
import common.helpers.ServletHelper;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "VehicleServlet")
public class VehicleServlet extends HttpServlet {

    final static Logger logger = Logger.getLogger(VehicleServlet.class);
    private static VehicleMakeDAO vehicleMakeDAO = new VehicleMakeDAOImpl();
    private static VehicleModelDAO vehicleModelDAO = new VehicleModelDAOImpl();
    private static VehicleDAO vehicleDAO = new VehicleDAOImpl();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//
        System.out.println(request.getParameter("formName"));

                    switch(request.getParameter("button")) {
                        case "update":
                            updateVehicle(request);
                            break;
                        case "delete":
                            deleteVehicle(request);
                            break;
                        case"add":
                            addVehicle(request);
                            break;

                        default:
                            break;
                    }

//
//        //notes: generate person dropdown using JSTL (logic is the same between forms)
        request.setAttribute("vehicleList",vehicleDAO.getVehicleList());
        request.setAttribute("vehicleMakeList",vehicleMakeDAO.getVehicleMakeList());
        request.setAttribute("vehicleModelList",vehicleModelDAO.getVehicleModelList());
        request.getRequestDispatcher("./vehicleSelector.jsp").forward(request, response);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setAttribute("vehicleMakeList",vehicleMakeDAO.getVehicleMakeList());
        request.setAttribute("vehicleModelList", vehicleModelDAO.getVehicleModelList());
        request.setAttribute("vehicleList", vehicleDAO.getVehicleList());

        request.setAttribute("selectVehicleMake", generateVehicleMakeDropDownHTML(0));
        request.setAttribute("selectedVehicleModel",generateVehicleModelDropDownHTML( 0));
   //     request.setAttribute("selectedVehicle",generateVehicleMenu());

        request.getRequestDispatcher("./vehicleSelector.jsp").forward(request, response);

    }

    private static void updateVehicle(HttpServletRequest request){
//        ServletHelper.logRequestParams(request);
        Vehicle addVehicle = new Vehicle();
        updateRequestToVehicle(request, addVehicle);
    }

    private static void deleteVehicle(HttpServletRequest request){
        int id = Integer.parseInt(request.getParameter("selId"));

        if(vehicleDAO.deleteVehicle(Integer.parseInt(request.getParameter("selId")))){
            System.out.println("vehicle deleted");
        }

    }


    private static String chooseVehicleMake(HttpServletRequest request){
        logger.info("Form #1 - Form Name=" + request.getParameter("formName"));
        ServletHelper.logRequestParams(request);

        //notes:    everything comes back from the request as a STRING
        String selectedVehicleMakeId = request.getParameter("selectVehicleMake");

        VehicleMake selectedVehicleMake = vehicleMakeDAO.getVehicleMakeById(Integer.parseInt(selectedVehicleMakeId));

        logger.info("selected VehicleMake Details: " + selectedVehicleMake.getVehicleMakeName());
        logger.info("selected VehicleMake Details: " + selectedVehicleMake.getVehicleMakeId());
        logger.info("selected VehicleMake Details: " + selectedVehicleMake.getCreateDate());

        vehicleMakeToRequest(request, selectedVehicleMake);

        request.setAttribute("selectVehicleMake", generateVehicleMakeDropDownHTML(selectedVehicleMake.getVehicleMakeId()));

        return selectedVehicleMakeId;
    }
//region
    private static void updateVehicleMake(HttpServletRequest request){
        logger.info("Form #2 - Form Name=" + request.getParameter("formName"));
        ServletHelper.logRequestParams(request);

        VehicleMake updateVehicleMake = new VehicleMake();
        requestToVehicleMake(request, updateVehicleMake);
  //    region
        logger.info(updateVehicleMake.getVehicleMakeName());
        logger.info(updateVehicleMake.getVehicleMakeId());
        logger.info(updateVehicleMake.getCreateDate());
 //       endregion
        if (vehicleMakeDAO.updateVehicleMake(updateVehicleMake))
            request.setAttribute("updateSuccessful", "Vehicle Make Updated in Database Successfully!");
        else
            request.setAttribute("updateSuccessful", "NO UPDATE FOR YOU");


        vehicleMakeToRequest(request, updateVehicleMake);

        //notes:    inefficient!! extra call to the database
//        updateVehicleMake = vehicleMakeDAO.getVehicleMakeById(updateVehicleMake.getVehicleMakeId());
//        vehicleMakeToRequest(request, updateVehicleMake);

        String vehicleMakeIdString = request.getParameter("vehicleMakeId");
        request.setAttribute("selectVehicleMake", generateVehicleMakeDropDownHTML(Integer.parseInt(vehicleMakeIdString)));
    }
    //endregion



    private static String generateVehicleMakeDropDownHTML(int selectedVehicleMakeId){
        //region
//                <select name="selectVehicleMake" id="">
//                <option value="1">Ford</option>
//                <option value="2">Volkswagen</option>
//                <option value="3">Chevrolet</option>
//                <option value="5">Pontiac</option>
//                <option value="6">Audi</option>
//                   </select>
        //endregion
        StringBuilder strBld = new StringBuilder();
        strBld.append("<select name='selectVehicleMake'>");
        strBld.append("<option value='0'>Select Vehicle Make</option>");
        for (VehicleMake vehicleMake : vehicleMakeDAO.getVehicleMakeList()){
            if(vehicleMake.getVehicleMakeId() == selectedVehicleMakeId){
                strBld.append("<option selected value='").append(vehicleMake.getVehicleMakeId()).append("'>").append(vehicleMake.getVehicleMakeName()).append("</option>");
            }
            else {
                strBld.append("<option value='").append(vehicleMake.getVehicleMakeId()).append("'>").append(vehicleMake.getVehicleMakeName()).append("</option>");
            }
        }
        strBld.append("</select>");
        return strBld.toString();
    }

    private static String generateVehicleModelDropDownHTML( int selectedVehicleModelId){
        StringBuilder strBld = new StringBuilder();
        strBld.append("<select name='selectVehicleModel'>");
        strBld.append("<option value='0'>Select Vehicle Model</option>");
        for(VehicleModel vehicleModel : vehicleModelDAO.getVehicleModelList()){
            if(vehicleModel.getVehicleModelId() == selectedVehicleModelId){
                strBld.append("<option selected value='").append(vehicleModel.getVehicleModelId()).append("'>").append(vehicleModel.getVehicleModelName()).append("</option>");

                }
                else {
                    strBld.append("<option value='").append(vehicleModel.getVehicleModelId()).append("'>").append(vehicleModel.getVehicleModelName()).append("</option>");
                }
        }
        return strBld.toString();

    }

//    private static String generateVehicleMenu(){
//        StringBuilder strBld = new StringBuilder();
//        for(Vehicle vehicle : vehicleDAO.getVehicleList()){
//            strBld.append("<input type=text placeholder='").append(vehicle.getLicensePlate()).append("'>");
//        }
//        return strBld.toString();
//    }

    private static void addVehicle(HttpServletRequest request){
        VehicleMake vehicleMake = vehicleMakeDAO.getVehicleMakeById(Integer.parseInt(request.getParameter("selectVehicleMake")));
        VehicleModel vehicleModel = vehicleModelDAO.getVehicleModelById(Integer.parseInt(request.getParameter("selectVehicleMake")));
        Vehicle vehicle = new Vehicle();
        Date date = new Date();
        vehicle.setYear(Integer.parseInt(request.getParameter("selYear")));
        vehicle.setLicensePlate(request.getParameter("selPlate"));
        vehicle.setColor(request.getParameter("selColor"));
        vehicle.setVin(request.getParameter("selVIN"));
        vehicle.setVehicleModel(vehicleModel);
        vehicle.setIsPurchase(false);
        vehicle.setPurchasePrice(0);
        vehicle.setPurchaseDate(DateHelper.utilDateToSqlDate(date));

        vehicleDAO.insertVehicle(vehicle);

    }

    private static void updateRequestToVehicle(HttpServletRequest request, Vehicle vehicle){
        VehicleModel vehicleModel = vehicleModelDAO.getVehicleModelById(Integer.parseInt(request.getParameter("selVehicleModel")));
      //  VehicleMake vehicleMake = vehicleMakeDAO.getVehicleMakeById(Integer.parseInt(request.getParameter("selVehicleMAKE")));
        Date date = new Date();

        vehicle.setYear(Integer.parseInt(request.getParameter("selYear")));
        vehicle.setLicensePlate(request.getParameter("selPlate"));
        vehicle.setColor(request.getParameter("selColor"));
        vehicle.setVin(request.getParameter("selVIN"));
        vehicle.setVehicleModel(vehicleModel);
        vehicle.setIsPurchase(false);
        vehicle.setPurchasePrice(0);
        vehicle.setPurchaseDate(DateHelper.utilDateToSqlDate(date));
        vehicle.setVehicleId(Integer.parseInt(request.getParameter("selId")));

        vehicleDAO.updateVehicle(vehicle);
    }

    private static void vehicleToRequest(HttpServletRequest request, Vehicle vehicle){
        request.setAttribute("vehicleId", vehicle.getVehicleId());

        request.setAttribute("", vehicle.getVehicleId());
        request.setAttribute("vehicleId", vehicle.getVehicleId());
        request.setAttribute("vehicleId", vehicle.getVehicleId());
        request.setAttribute("vehicleId", vehicle.getVehicleId());
        request.setAttribute("vehicleId", vehicle.getVehicleId());
        request.setAttribute("vehicleId", vehicle.getVehicleId());
        request.setAttribute("vehicleId", vehicle.getVehicleId());
        request.setAttribute("vehicleId", vehicle.getVehicleId());
    }

    private static void requestToVehicleMake(HttpServletRequest request, VehicleMake vehicleMake){
        //notes:    everything comes back from the request as a STRING!!!
        vehicleMake.setVehicleMakeId(Integer.parseInt(request.getParameter("vehicleMakeId")));
        vehicleMake.setVehicleMakeName(request.getParameter("vehicleMakeName"));
        vehicleMake.setCreateDate(DateHelper.stringToUtilDate(request.getParameter("vehicleMakeCreateDate"), "yyyy-MM-dd"));
    }

    private static void vehicleMakeToRequest(HttpServletRequest request, VehicleMake vehicleMake){
        request.setAttribute("vehicleMakeName", vehicleMake.getVehicleMakeName());
        request.setAttribute("vehicleMakeId", vehicleMake.getVehicleMakeId());
        request.setAttribute("vehicleMakeCreateDate", DateHelper.utilDateToSqlDate(vehicleMake.getCreateDate()));
    }

}
