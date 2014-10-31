package com.store.api.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.store.api.mysql.entity.search.SearchUpdateVehicleInfoVo;
import com.store.api.mysql.entity.webService.VeUpdateReturnVo;
import com.store.api.mysql.service.SearchEngineService;
import com.store.api.utils.JsonUtils;
import com.store.api.utils.Method;
import com.store.api.utils.Utils;

@Controller()
@Scope("prototype")
@RequestMapping("/V1/search")
public class SearchAction extends BaseAction {
	
	
	private static final String  name =  "sfc365";
	private static final String  passwd = "88888888";

    @Autowired
    private SearchEngineService searchEngineService;

    private Map<String, Object> result = new HashMap<String, Object>();

    /**
     * 按坐标经纬度查询坐标地址
     * 
     * @return
     */
    @ResponseBody
    @RequestMapping("/geosearch")
    public Map<String, Object> geoSearch(@RequestParam(value = "lng", required = false, defaultValue = "0")
    Double lng, @RequestParam(value = "lat", required = false, defaultValue = "0")
    Double lat) {
        try {
            String resultStr = "";
            if (lng == 0 || lat == 0) {
                result.put("errorcode", "2");
                result.put("info", "参数错误");
                result.put("data", resultStr);
                return result;
            }

            resultStr = searchEngineService.getGeoInfo(lng, lat);
            result.put("errorcode", "0");
            result.put("info", "成功");
            result.put("data", resultStr);
            LOG.info(resultStr);
            return result;
        } catch (Exception e) {
            LOG.error("geosearch fail.", e);
            result.put("errorcode", "3");
            result.put("info", "服务器忙晕了，请稍后再试");
            result.put("data", "");
            return result;
        }
    }

    /**
     * 更新搜索引擎的车辆信息
     * 
     * @param vehicleId
     * @param status
     * @param vehiclePhone
     * @param driveName
     * @param starLevel
     * @param lng
     * @param brand
     * @param vehicleLength
     * @param vehicleLoad
     * @param vehicleType
     * @param cargoList
     * @param operator
     * @param visible
     * @param domicileLng
     * @param domicileLat
     * @param LocationLng
     * @param LocationLat
     * @return
     */
    @ResponseBody
    @RequestMapping("/updatevehicle")
    public Map<String, Object> updatevehicle(
    		@RequestParam(value = "token", required = false)
    	    String token,
    		@RequestParam(value = "vehicleId", required = false)
    Long vehicleId, @RequestParam(value = "online", required = false)
    Boolean online, @RequestParam(value = "status", required = false)
    Long status, @RequestParam(value = "vehiclePhone", required = false)
    String vehiclePhone, @RequestParam(value = "driveName", required = false)
    String driveName, @RequestParam(value = "starLevel", required = false)
    Long starLevel, @RequestParam(value = "imageUrl", required = false)
    String imageUrl, @RequestParam(value = "brand", required = false)
    Long brand, @RequestParam(value = "vehicleLength", required = false)
    Long vehicleLength, @RequestParam(value = "vehicleLoad", required = false)
    Long vehicleLoad, @RequestParam(value = "vehicleType", required = false)
    Long vehicleType, @RequestParam(value = "cargoList", required = false)
    String cargoList, @RequestParam(value = "operator", required = false)
    Long operator, @RequestParam(value = "visible", required = false)
    Boolean visible, @RequestParam(value = "domicileLng", required = false)
    Double domicileLng, @RequestParam(value = "domicileLat", required = false)
    Double domicileLat, @RequestParam(value = "LocationLng", required = false)
    Double LocationLng, @RequestParam(value = "LocationLat", required = false)
    Double LocationLat, @RequestParam(value = "ownerLevel", required = false)
    Long ownerLevel, @RequestParam(value = "plate", required = false)
    	    String plate
    ) {

    	
    	if(vehicleId == null){
    		result.put("errorcode", "2");
            result.put("info", "参数错误，车辆id不能为空");
            return result;
    	}
    	String thisToken = ""+Method.MD5(name+passwd+vehicleId);
      	if(Utils.isEmpty(token) ||!token.equalsIgnoreCase(thisToken) ){
              result.put("errorcode", "4");
              result.put("info", "令牌错误");
              result.put("data", "");
              return result;
      	}
        SearchUpdateVehicleInfoVo vo = new SearchUpdateVehicleInfoVo();
        vo.setVehicleId(vehicleId);
        vo.setOnline(online);
        vo.setStatus(status);
        vo.setVehiclePhone(vehiclePhone);
        vo.setDriveName(driveName);
        vo.setStarLevel(starLevel);
        vo.setImageUrl(imageUrl);
        vo.setBrand(brand);
        vo.setVehicleLength(vehicleLength);
        vo.setVehicleType(vehicleType);
        vo.setCargoList(cargoList);
        vo.setOperator(operator);
        vo.setVisible(visible);
        Double[] domicileArray = new Double[2];
        domicileArray[0] = domicileLng;
        domicileArray[1] = domicileLat;
        vo.setDomicile(domicileArray);
        Double[] locationArray = new Double[2];
        locationArray[0] = LocationLng;
        locationArray[1] = LocationLat;
        vo.setLocation(locationArray);
        vo.setOwnerLevel(ownerLevel);
        vo.setPlate(plate);
        try {
            VeUpdateReturnVo resultStr = searchEngineService.updateVehilceInfo(vo);
            if(resultStr != null && resultStr.getResult().equals("Success")){
           	 result.put("errorcode", "0");
                result.put("info", "成功");
                result.put("data", resultStr);
                return result;
           }else {
	             result.put("errorcode", "1");
	             result.put("info", "失败");
	             result.put("data", resultStr);
	             return result;
           }

        } catch (Exception e) {
            LOG.error("updatevehicle fail.", e);
            result.put("errorcode", "3");
            result.put("info", "服务器忙晕了，请稍后再试");
            result.put("data", "");
            return result;
        }

    }

    /**
     * 新增搜索引擎的车辆信息
     * 
     * @param vehicleId
     * @param status
     * @param vehiclePhone
     * @param driveName
     * @param starLevel
     * @param lng
     * @param brand
     * @param vehicleLength
     * @param vehicleLoad
     * @param vehicleType
     * @param cargoList
     * @param operator
     * @param visible
     * @param domicileLng
     * @param domicileLat
     * @param LocationLng
     * @param LocationLat
     * @return
     */
    @ResponseBody
    @RequestMapping("/insertvehicle")
    public Map<String, Object> insertvehicle(
    		@RequestParam(value = "token", required = false)
    	    String token,
    		@RequestParam(value = "vehicleId", required = false)
    Long vehicleId, @RequestParam(value = "plate", required = false)
    String plate, @RequestParam(value = "status", required = false)
    Long status, @RequestParam(value = "vehiclePhone", required = false)
    String vehiclePhone, @RequestParam(value = "driveName", required = false)
    String driveName, @RequestParam(value = "starLevel", required = false)
    Long starLevel, @RequestParam(value = "imageUrl", required = false)
    String imageUrl, @RequestParam(value = "brand", required = false)
    Long brand, @RequestParam(value = "vehicleLength", required = false)
    Long vehicleLength, @RequestParam(value = "vehicleLoad", required = false)
    Long vehicleLoad, @RequestParam(value = "vehicleType", required = false)
    Long vehicleType, @RequestParam(value = "cargoList", required = false)
    String cargoList, @RequestParam(value = "operator", required = false)
    Long operator, @RequestParam(value = "visible", required = false)
    Boolean visible, @RequestParam(value = "domicileLng", required = false)
    Double domicileLng, @RequestParam(value = "domicileLat", required = false)
    Double domicileLat, @RequestParam(value = "LocationLng", required = false)
    Double LocationLng, @RequestParam(value = "LocationLat", required = false) 
    Double LocationLat, 
    @RequestParam(value = "ownerLevel", required = false, defaultValue="0")Long ownerLevel,
    @RequestParam(value = "identityAuth", required = false, defaultValue = "1" )Boolean identityAuth,
    @RequestParam(value = "startingPrice",required = false,defaultValue = "0") Double startingPrice,
    @RequestParam(value = "startingMileage",required = false , defaultValue = "0") Double startingMileage,
    @RequestParam(value = "mileagePrice" ,required = false ,defaultValue = "0") Double mileagePrice ) {
    	
    	if(vehicleId == null){
    		result.put("errorcode", "2");
            result.put("info", "参数错误，车辆id不能为空");
            return result;
    	}
    	 String thisToken = ""+Method.MD5(name+passwd+vehicleId);
     	if(Utils.isEmpty(token) ||!token.equalsIgnoreCase(thisToken) ){
             result.put("errorcode", "4");
             result.put("info", "令牌错误");
             result.put("data", "");
             return result;
     	}
        SearchUpdateVehicleInfoVo vo = new SearchUpdateVehicleInfoVo();
        vo.setVehicleId(vehicleId);
        vo.setPlate(plate);
        vo.setStatus(status);
        vo.setVehiclePhone(vehiclePhone);
        vo.setDriveName(driveName);
        vo.setStarLevel(starLevel);
        vo.setImageUrl(imageUrl);
        vo.setBrand(brand);
        vo.setVehicleLength(vehicleLength);
        vo.setVehicleType(vehicleType);
        vo.setCargoList(cargoList);
        vo.setOperator(operator);
        vo.setVisible(visible);
        Double[] domicileArray = new Double[2];
        domicileArray[0] = domicileLng;
        domicileArray[1] = domicileLat;
        vo.setDomicile(domicileArray);
        Double[] locationArray = new Double[2];
        locationArray[0] = LocationLng;
        locationArray[1] = LocationLat;
        vo.setLocation(locationArray);
        
        vo.setOwnerLevel(ownerLevel);
        vo.setIdentityAuth(identityAuth);
        vo.setStartingMileage(startingMileage);
        vo.setStartingPrice(startingPrice);
        vo.setMileagePrice(mileagePrice);
        try {
            VeUpdateReturnVo resultStr = searchEngineService.insertVehicleInfo(vo);
            if(resultStr != null && resultStr.getResult().equals("Success")){
            	 result.put("errorcode", "0");
                 result.put("info", "成功");
                 result.put("data", resultStr);
                 return result;
            }else {
	             result.put("errorcode", "1");
	             result.put("info", "失败");
	             result.put("data", resultStr);
	             return result;
            }
           

        } catch (Exception e) {
            LOG.error("insertvehicle fail.", e);
            result.put("errorcode", "3");
            result.put("info", "服务器忙晕了，请稍后再试");
            result.put("data", "");
            return result;
        }

    }

    /**
     * 删除搜索引擎车辆信息
     * 
     * @param vehicleId
     *            车辆id
     * @return
     */
    @ResponseBody
    @RequestMapping("/deletevehicle")
    public Map<String, Object> deletevehicle(
    		@RequestParam(value = "vehicleId", required = false)
    Long vehicleId,
    		@RequestParam(value = "token", required = false)
    String token) {
        try {
        	if(vehicleId == null){
        		result.put("errorcode", "2");
                result.put("info", "参数错误，车辆id不能为空");
                return result;
        	}
        	String thisToken = ""+Method.MD5(name+passwd+vehicleId);
          	if(Utils.isEmpty(token) ||!token.equalsIgnoreCase(thisToken) ){
                  result.put("errorcode", "4");
                  result.put("info", "令牌错误");
                  result.put("data", "");
                  return result;
          	}
            VeUpdateReturnVo resultStr = searchEngineService.deleteVehicleInfo(vehicleId);
            if(resultStr != null && resultStr.getResult().equals("Success")){
           	 result.put("errorcode", "0");
                result.put("info", "成功");
                result.put("data", resultStr);
                return result;
           }else {
	             result.put("errorcode", "1");
	             result.put("info", "失败");
	             result.put("data", resultStr);
	             return result;
           }
        } catch (Exception e) {
            LOG.error("deletevehicle fail.", e);
            result.put("errorcode", "3");
            result.put("info", "服务器忙晕了，请稍后再试");
            result.put("data", "");
            return result;
        }
    }

    /**
     * 获取车辆信息
     * @param phone  手机号
     * @param plate  车牌号
     * @return
     */
    @ResponseBody
    @RequestMapping("/getvehicleinfo")
    public Map<String, Object> getVehicleInfo(@RequestParam(value = "phone", required = false, defaultValue = "")
    String phone, @RequestParam(value = "plate", required = false, defaultValue = "")
    String plate) {
    	
        try {
            if (Utils.isEmpty(phone) && Utils.isEmpty(plate)) {
                result.put("errorcode", "2");
                result.put("info", "参数错误");
                result.put("data", "");
                return result;
            }
           
            String resultStr = searchEngineService.getVihicleInfo(phone, plate);
            result.put("errorcode", "0");
            result.put("info", "成功");
            result.put("data", resultStr);
            return result;
        } catch (Exception e) {
            LOG.error("deletevehicle fail.", e);
            result.put("errorcode", "3");
            result.put("info", "服务器忙晕了，请稍后再试");
            result.put("data", "");
            return result;
        }
    }

    @ResponseBody
    @RequestMapping("/updatevehicleonline")
    public Map<String, Object> updateVehicleOnline(
    		@RequestParam(value = "phone", required = false)
    String phone, @RequestParam(value = "online", required = false)
    Boolean online ,@RequestParam(value = "token", required = false)
    	    String token) {
    	
    	
        try {
        	
            if (Utils.isEmpty(phone)) {
                result.put("errorcode", "2");
                result.put("info", "参数错误");
                result.put("data", "");
                return result;
            }
            String thisToken = ""+Method.MD5(name+passwd+phone);
        	if(Utils.isEmpty(token) ||!token.equalsIgnoreCase(thisToken) ){
                result.put("errorcode", "4");
                result.put("info", "令牌错误");
                result.put("data", "");
                return result;
        	}
	        VeUpdateReturnVo resultStr = searchEngineService.updateVehicleOnline(phone, online);
	        if(resultStr != null && resultStr.getResult().equals("Success")){
	          	 result.put("errorcode", "0");
	               result.put("info", "成功");
	               result.put("data", resultStr);
	               return result;
	          }else {
	             result.put("errorcode", "1");
	             result.put("info", "失败");
	             result.put("data", resultStr);
	             return result;
	          }
	        
        } catch (Exception e) {
            LOG.error("deletevehicle fail.", e);
            result.put("errorcode", "3");
            result.put("info", "服务器忙晕了，请稍后再试");
            result.put("data", "");
            return result;
        }
    }
    
    
}
