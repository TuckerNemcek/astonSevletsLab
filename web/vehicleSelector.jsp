<%--
  Created by IntelliJ IDEA.
  User: tucker
  Date: 12/19/19
  Time: 1:20 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <link rel="stylesheet" href="./static/css/site.css" />
    <title>Vehicle Selector</title>
</head>
<body>
<br>
    <div class="border">

        <form name="addVehicle" action="./vehicleSelector" method="post">
            <input type="hidden" name="formName" value="addVehicle" />
            <select id="veMake" name="selectVehicleMake">
                    <option value='0'>Select Vehicle Make</option>
                <c:forEach var="vehicleMake" items="${vehicleMakeList}">
                    <c:choose>
                        <c:when test="${vehicleMake.vehicleMakeId == vehicleMakeId}">
                            <option selected value="${vehicleMake.vehicleMakeId}">${vehicleMake.vehicleMakeName}</option>
                        </c:when>
                        <c:otherwise>
                            <option value="${vehicleMake.vehicleMakeId}">${vehicleMake.vehicleMakeName}</option>
                        </c:otherwise>
                    </c:choose>
            </c:forEach>
            </select>

            <br>
            <br>


            <input type="hidden" name="formName" value="chooseVehicleModel" />
            <select id="veModel" name="selectVehicleModel">
                <option value='0'>Select Vehicle Model</option>
                <c:forEach var="vehicleModel" items="${vehicleModelList}">
                    <c:choose>
                        <c:when test="${vehicleModel.vehicleModelId == vehicleModelId}">
                            <option selected value="${vehicleModel.vehicleModelId}">${vehicleModel.vehicleModelName}</option>
                        </c:when>
                        <c:otherwise>
                            <option value="${vehicleModel.vehicleModelId}">${vehicleModel.vehicleModelName}</option>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            </select>


            <br>
            <br>

            Plate:  <input type ="text" style="margin-left: 20px;" name="selPlate" value="${selPlate}"><br><br>
            VIN:    <input type ="text" style="margin-left: 20px;" name="selVIN" value="${selVIN}"><br><br>
            Year:   <input type ="text" style="margin-left: 20px;" name="selYear" value="${selYear}"><br><br>
            Color:  <input type ="text" style="margin-left: 13px;" name="selColor" value="${selColor}"><br><br>
            <button type="submit" name="button" value="add">Add Vehicle</button>
        </form>

    </div>

<div class="border2 displayVehicles">

    <div class="displayHeader">

        <span id="plate" class="topRow">Plate</span>
        <span id="vin" class="topRow">VIN</span>
        <span id="year" class="topRow">Year</span>
        <span id="color" class="topRow">Color</span>
        <span id="make" class="topRow">Make</span>
        <span id="model" class="topRow">Model</span>

        <hr class="bar">
    </div>

    <hr>

    <div class="vehicleContainer">

        <div class="vehicleRow">
            <c:forEach var="vehicle" items="${vehicleList}">
                <div class="vehicleRow">
                    <form  name="updateOrDeleteVehicle" action="./vehicleSelector" method="post">
                        <input type="hidden" name="formName" value="updateOrDeleteVehicle"/>
                        <input name="selId" class="item" type="hidden"  value=${vehicle.vehicleId} />
                    <input name="selPlate" size="8" class="item" type="text" name=${vehicle} value=${vehicle.licensePlate}>
                    <input name="selVIN" size="25" class="item" type="text" class="vehicleItem" name=${vehicle} value=${vehicle.vin}>
                    <input name="selYear" size="4" class="item" type="text" class="vehicleItem" name=${vehicle} value=${vehicle.year}>
                    <input name="selColor" size="10" class="item" type="text" class="vehicleItem" name=${vehicle} value=${vehicle.color}>
<%--                    <p>${vehicle.vehicleModel.vehicleModelId}</p>--%>

                    <select name="selVehicleModel">
                        <c:forEach var="model" items="${vehicleModelList}">
                            <c:choose>
                                <c:when test="${vehicle.vehicleModel.vehicleModelId == model.vehicleModelId}">
                                    <option selected value="${model.vehicleModelId}">${model.vehicleModelName}</option>
                                </c:when>
                                <c:otherwise>
                                    <option value="${model.vehicleModelId}">${model.vehicleModelName}</option>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                      </select>


                    <select name="selVehicleMake">
                        <c:forEach var="make" items="${vehicleMakeList}">
                            <c:choose>
                                <c:when test="${vehicle.vehicleModel.vehicleMake.vehicleMakeId == make.vehicleMakeId}">
                                    <option selected value="${make.vehicleMakeId}">${make.vehicleMakeName}</option>
                                </c:when>
                                <c:otherwise>
                                    <option value="${make.vehicleMakeId}">${make.vehicleMakeName}</option>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </select>

                    <button class="item" type="submit" name="button" value="update">Update Vehicle</button>
                    <button class="item" type="submit" name="button" value="delete">Delete Vehicle</button>
                    </form>
                </div>

            </c:forEach>
</div>


    </div>


</div>
    <br>
    <br>

</body>
</html>
