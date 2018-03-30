import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.AddressComponent;
import com.google.maps.model.AddressComponentType;
import com.google.maps.model.AddressType;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;

public class MainApplication 

{ 
 public static	int count = 0;
	public static void main(String[] args)
	{
		Long startTime = System.currentTimeMillis();
		String json = NetworkUtils.getJSONDataFromUrl("https://maps.googleapis.com/maps/api/directions/json?origin=Jaipur+Rajasthan+India&destination=Ahmedabad+India&alternatives=true&key=AIzaSyADGp-GKcaCEq59ZdS_s1w3Yy4anTF3Xsw");
	//	System.out.println("json" + json);
		ArrayList<String> mainCityArray = new ArrayList<>();
		JSONObject jsonObj;
		try {
			ArrayList<ArrayList<String>> mainList = new ArrayList<>();
			jsonObj = new JSONObject(json);
			JSONArray jsonRoutesArray = jsonObj.getJSONArray("routes");
			for (int outer = 0; outer < jsonRoutesArray.length(); outer++) {
				JSONObject routesObject = jsonRoutesArray.getJSONObject(outer);
				JSONArray jsonLegsArray = routesObject.getJSONArray("legs");
				JSONObject legsObject = jsonLegsArray.getJSONObject(0);
				JSONArray stepsArray = legsObject.getJSONArray("steps");

				for (int i = 0; i < stepsArray.length(); i++) {
					JSONObject stepObj = stepsArray.getJSONObject(i);
					// System.out.println("Steps = " + stepObj);
					JSONObject startLocObj = stepObj.getJSONObject("start_location");
					JSONObject endLocObj = stepObj.getJSONObject("end_location");
					mainList.add(getLatLongFromJSONObject(startLocObj));
					mainList.add(getLatLongFromJSONObject(endLocObj));
				}

				for (ArrayList<String> latLongList : mainList) {
					String lat = latLongList.get(0);
					String lon = latLongList.get(1);
					// reversePlaceLookup(lat, lon);
					// testFunction(lat, lon);
					mainCityArray.addAll(testFunction2(lat, lon));
				}

			}
			 System.out.println("RESULT ALL:-" + mainCityArray);
			 Set<String> uniqueCity = new HashSet<String>(mainCityArray);
			 System.out.println("RESULT UNIQUE:-" + uniqueCity);
			 ArrayList<String> Orderedlist = new ArrayList<String>(new LinkedHashSet<String>(mainCityArray));
			 System.out.println("RESULT UNIQUE (ORDERED):-" + Orderedlist);
			 //System.out.println("Total calls count: " + count);
			 Long endTime = System.currentTimeMillis();
			 System.out.println("TOTAL TIME (in seconds):  "+ ( (double) ((Long)(endTime-startTime)/1000))  );
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static ArrayList<String> getLatLongFromJSONObject(JSONObject obj) throws Exception
	{
		ArrayList<String> latLongList = new ArrayList<String>();
		String lat = obj.getString("lat");
		String lng = obj.getString("lng");
		latLongList.add(lat);
		latLongList.add(lng);		
		return latLongList;
	}
	
	private static void reversePlaceLookup(String lat, String lng) throws Exception
	{
		try {
            String json = NetworkUtils.getJSONDataFromUrl("http://maps.googleapis.com/maps/api/geocode/json?latlng=" + lat + ","
                    + lng + "&sensor=true");
            
             JSONObject jsonObj = new JSONObject(json);
             String finalAddress = "";
            String Status = jsonObj.getString("status");
            if (Status.equalsIgnoreCase("OK")) {
                JSONArray Results = jsonObj.getJSONArray("results");
                JSONObject location = Results.getJSONObject(0);
                finalAddress = location.getString("formatted_address");
                System.out.println("WAYPOINTS --> " + finalAddress);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

	private static void testFunction(String lat,String lng) throws Exception
	{
		ArrayList<String> pincodesArray = new ArrayList<>();
  		Double latD = Double.parseDouble(lat);
		Double lngD = Double.parseDouble(lng);
		GeoApiContext context = new GeoApiContext().setApiKey("AIzaSyADGp-GKcaCEq59ZdS_s1w3Yy4anTF3Xsw");
		//GeocodingResult[] results =  GeocodingApi.newRequest(context).latlng(new LatLng(40.714224, -73.961452)).language("en").resultType(AddressType.COUNTRY, AddressType.ADMINISTRATIVE_AREA_LEVEL_1).await();
		GeocodingResult[] results =  GeocodingApi.newRequest(context).latlng(new LatLng(latD, lngD)).language("en").resultType(AddressType.COUNTRY, AddressType.ADMINISTRATIVE_AREA_LEVEL_1).await();
		
		
	
	for(GeocodingResult r:results)
	{
	for (AddressComponent ac : r.addressComponents) {
        for (AddressComponentType acType : ac.types) {

            if (acType == AddressComponentType.ADMINISTRATIVE_AREA_LEVEL_2) 
            {
            	System.out.println("STATE=" + ac.longName);

            } else if (acType == AddressComponentType.LOCALITY) 
            {
            		System.out.println("CITY=" + ac.longName);

            } else if (acType == AddressComponentType.POSTAL_CODE) 
            {
            	pincodesArray.add(ac.longName);
            }
        }
	
	}}
	
}	
	
	private static ArrayList<String> testFunction2(String lat,String lng) throws Exception
	{
		ArrayList<String> cityArray = new ArrayList<>();
  		Double latD = Double.parseDouble(lat);
		Double lngD = Double.parseDouble(lng);
		count ++;
		GeoApiContext context = new GeoApiContext().setApiKey("AIzaSyADGp-GKcaCEq59ZdS_s1w3Yy4anTF3Xsw");
		//GeocodingResult[] results =  GeocodingApi.newRequest(context).latlng(new LatLng(40.714224, -73.961452)).language("en").resultType(AddressType.COUNTRY, AddressType.ADMINISTRATIVE_AREA_LEVEL_1).await();
		GeocodingResult[] results =  GeocodingApi.newRequest(context).latlng(new LatLng(latD, lngD)).language("en").resultType(AddressType.COUNTRY, AddressType.LOCALITY).await();
		
		for (GeocodingResult r : results) {
			for (AddressComponent ac : r.addressComponents) {
				for (AddressComponentType acType : ac.types) {

					if (acType == AddressComponentType.LOCALITY) {
						System.out.println("CITY=" + ac.longName);
						cityArray.add(ac.longName);
					} else if (acType == AddressComponentType.ADMINISTRATIVE_AREA_LEVEL_2) {
						//System.out.println("STATE=" + ac.longName);

					} else if (acType == AddressComponentType.POSTAL_CODE) {
					}
				}

			}
		}
	return cityArray;
}	
}
