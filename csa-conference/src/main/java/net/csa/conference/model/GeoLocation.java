package net.csa.conference.model;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.log4j.Logger;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * @author philipp.amkreutz
 */

@Document
public class GeoLocation {

	private static final Logger log = Logger.getLogger(GeoLocation.class);

	@Transient
	private String googleMapsApiKey = "AIzaSyDOwyBEQP7DM0JX7bD758a8oSJBXDW535o";

	private String address;

	private Double longitude;
	private Double latitude;

	public GeoLocation() {

	}

	private String createRequestUrl() throws UnsupportedEncodingException {
		String url = "https://maps.googleapis.com/maps/api/geocode/json?address=" + URLEncoder.encode(address, "UTF-8") + "&key=" + googleMapsApiKey;
		return url;
	}

	public void requestGeoLocaion() {
		try {
			URL url = new URL(createRequestUrl());
			HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Accept", "application/json");
			BufferedReader bf = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String output = bf.readLine();
			StringBuilder sb = new StringBuilder();
			while(output != null) {
				sb.append(output);
				output = bf.readLine();
			}
			String result = sb.toString();
			JsonObject json = new Gson().fromJson(result, JsonObject.class);
			String responseCode = json.get("status").getAsString();
			if (responseCode.equals("OK")) {
				JsonArray results = json.getAsJsonArray("results");
				JsonObject arrayContent = results.get(0).getAsJsonObject();
				JsonObject geometry = arrayContent.get("geometry").getAsJsonObject();
				JsonObject location = geometry.get("location").getAsJsonObject();
				JsonElement lng = location.get("lng");
				JsonElement lat = location.get("lat");
				this.longitude = lng.getAsDouble();
				this.latitude = lat.getAsDouble();
			}
			log.info("GoogleMapsAPI GeoLocation Response :" + responseCode + " for '" + createRequestUrl() + "'");
		} catch (IOException e) {
			e.printStackTrace();
			log.info("Error while handling GoogleMapsAPI GeoLocation Response");
			return;
		}
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Double getLongitude() {
		return this.longitude;
	}

	public Double getLatitude() {
		return this.latitude;
	}

}
