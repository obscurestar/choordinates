package dischord;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
public class Data {
	public static String filename = "dischord.json";
	
    public static void main(String[] args)
    {
    }
    
    public void write()
    {
        try
        {
            // Create an ObjectMapper instance
            ObjectMapper objectMapper = new ObjectMapper();
            
            //Write a test file.
            DischordData data = new DischordData();
            data.setLastTuning("EADGBE 6 String");

            // Convert the object to JSON string
            String jsonString = objectMapper.writeValueAsString(data);
            
            // Specify the path to your JSON file
            File jsonFile = new File(filename);
            
            objectMapper.writeValue(new File(filename), data);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public void read()
    {
        try
        {
            // Create an ObjectMapper instance
            ObjectMapper objectMapper = new ObjectMapper();
                        
            // Specify the path to your JSON file
            File jsonFile = new File(filename);
            
            // Read the JSON file and map it to a Java object
            DischordData myObject = objectMapper.readValue(jsonFile, DischordData.class);

            // Print the loaded object
            System.out.println(myObject.last_tuning);

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }    	
    }
}

//The data structure for reading and writing
class DischordData
{
	public String last_tuning;
	
	@JsonProperty("last_tuning")
	public void setLastTuning(String last_tuning)
	{
		this.last_tuning = last_tuning;
	}
	
}