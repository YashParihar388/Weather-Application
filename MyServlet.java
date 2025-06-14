package MyPackage;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.util.Scanner;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class MyServlet
 */
@WebServlet("/MyServlet")
public class MyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MyServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		//set up apii
		String inputData = request.getParameter("city");
		if (inputData == null || inputData.trim().isEmpty()) {
		    response.getWriter().println("City name is required.");
		    return;
		}
		String apiKey = "2b9f12c89e381d359820ed4a6988345d";
		// Get the city from the form input
        String city = request.getParameter("city"); 

        // Create the URL for the OpenWeatherMap API request
        String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey;
        
//        try {
        try {
        //Api integration
        URL url=new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();//type casting
        connection.setRequestMethod("GET");//calling get method taking request from get method;
        
        //reading the data from network
        InputStream inputStream = connection.getInputStream();
        InputStreamReader reader = new InputStreamReader(inputStream);
        
        //taking input from reader and now want to store in string

        Scanner scanner = new Scanner(reader);
        StringBuilder responseContent = new StringBuilder();
        
        //applying while loop to fully read the file
        while (scanner.hasNext()) {
            responseContent.append(scanner.nextLine());//nextline is for reading next line wrna infinity loop chlega
        }
        
        	scanner.close();
//         Parse the JSON response to extract temperature, date, and humidity
        	Gson gson = new Gson();
        	JsonObject jsonObject = gson.fromJson(responseContent.toString(), JsonObject.class);
        	
        //Date & Time
		    long dateTimestamp = jsonObject.get("dt").getAsLong() * 1000;
		    String date = new Date(dateTimestamp).toString();
//		    
//		    //Temperature
		    double temperatureKelvin = jsonObject.getAsJsonObject("main").get("temp").getAsDouble();
		    int temperatureCelsius = (int) (temperatureKelvin - 273.15);
//		   
//		    //Humidity
		    int humidity = jsonObject.getAsJsonObject("main").get("humidity").getAsInt();
//		    
//		    //Wind Speed
		    double windSpeed = jsonObject.getAsJsonObject("wind").get("speed").getAsDouble();
//		    
//		    //Weather Condition
		    String weatherCondition = jsonObject.getAsJsonArray("weather").get(0).getAsJsonObject().get("main").toString();
//		    
//		 // Set the data as request attributes (for sending to the jsp page)
		    request.setAttribute("date", date);
		    request.setAttribute("city", city);
		    request.setAttribute("temperature", temperatureCelsius);
		    request.setAttribute("weatherCondition", weatherCondition); 
		    request.setAttribute("humidity", humidity);    
		    request.setAttribute("windSpeed", windSpeed);
		    request.setAttribute("weatherData", responseContent.toString());
		    
        	connection.disconnect();
        }catch(IOException e) {
        	e.printStackTrace();        }
        // Forward the request to the weather.jsp page for rendering
        	request.getRequestDispatcher("index.jsp").forward(request, response);
        

	}

}