package com.mit.google.services;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.google.api.client.util.Value;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mit.address.entities.Address;
import com.mit.map.entities.Coordinate;
import com.mit.utils.JsonUtils;

@Service
public class GoogleService {
	private static final String CITY_TYPE = "administrative_area_level_2";
	private static final String STATE_TYPE = "administrative_area_level_1";
	private static final String COUNTRY_TYPE = "country";
	
	@Value("${google.map.api.key}")
	private String GOOGLE_MAP_API_KEY = "AIzaSyBeMV9bSPJqVxJ7iOgavveX7dPTC5_svKI";
	
	public List<Address> getAddress(String key) {
		List<Address> addresses = new LinkedList<>();
		try {
			key = key.replace(",", "+");
			key = key.replace(" ", "+");

			URL url = new URL("https://maps.googleapis.com/maps/api/geocode/json?address=" + key + "&key="
					+ GOOGLE_MAP_API_KEY);
			HttpURLConnection connect = (HttpURLConnection) url.openConnection();
			JsonParser jParser = new JsonParser();
			JsonObject jObject = jParser.parse(new InputStreamReader(connect.getInputStream())).getAsJsonObject();
			JsonArray jArr = jObject.get("results").getAsJsonArray();
			for (JsonElement jEle : jArr) {
				JsonObject jEleO = jEle.getAsJsonObject();
				Address address = new Address();
				System.out.println(jEle);
				JsonObject coordinate = jEleO.get("geometry").getAsJsonObject().get("location").getAsJsonObject();
				Coordinate point = new Coordinate(coordinate.get("lng").getAsDouble(), coordinate.get("lat").getAsDouble());
				address.setCoordinate(point);
				
				address.setFullAddress(jEleO.get("formatted_address").getAsString());
				
				JsonArray addComponents = jEleO.get("address_components").getAsJsonArray();
				for (JsonElement component : addComponents) {
					JsonObject componentO = component.getAsJsonObject();
					String types = componentO.get("types").toString();
					if (types.contains(CITY_TYPE)) {
						address.setCity(componentO.get("long_name").getAsString());
					} else if (types.contains(STATE_TYPE)) {
						address.setState(componentO.get("long_name").getAsString());
					} else if (types.contains(COUNTRY_TYPE)) {
						address.setCountry(componentO.get("long_name").getAsString());
						address.setCountryCode(componentO.get("short_name").getAsString());
					}
				}
				
				addresses.add(address);
			}
		} catch (Exception e) {
			
		}

		return addresses;
	}
	
	public static void main(String[] args) {
		GoogleService service = new GoogleService();
		String address = "US";
		List<Address> json = service.getAddress(address);
		System.out.println(JsonUtils.Instance.toJson(json));
	}
}
