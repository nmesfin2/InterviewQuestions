package com.ebcon.punchlogictest;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class App 
{
	public static void main(String[] args){
		
		// build the job meta data;
		Map<String, JobMeta> jobMap = buildJobMap();		
		
		// calculate rates/wages based on the number of hours worked and work type
		Map<String, JSONObject> payrollResult = calculatePayroll(jobMap);	
		
		// convert to the output result to json object
		JSONObject jo = convertMapToJson(payrollResult);	
		System.out.println(jo);
		
		
	}
	
	public static Map<String, JobMeta> buildJobMap(){
		Map<String, JobMeta> jobMap = new HashMap<String, JobMeta>();
		JSONParser parser = new JSONParser();
		try {
			JSONArray  jsonObject = (JSONArray ) parser.parse(new FileReader("src\\main\\java\\com\\ebcon\\punchlogictest\\jobMeta.json"));
			
			// read the jobMeta data and create a map for later use.
			for(Object o : jsonObject) {
				JSONObject jobdata = (JSONObject) o;
				String name = (String) jobdata.get("job");
				double rate = (Double) jobdata.get("rate");
				double benefitsRate = (Double) jobdata.get("benefitsRate");
				JobMeta jm = new JobMeta(name, rate, benefitsRate);
				jobMap.put(name, jm);
			}
			return jobMap;
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public static Map<String, JSONObject> calculatePayroll(Map<String, JobMeta> jobMap){
		Map<String, JSONObject> employeeDataMap = new HashMap<String, JSONObject>();
		String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";
		DateFormat dateFormatter = new SimpleDateFormat(DEFAULT_PATTERN);
		
		
		JSONParser parser = new JSONParser();
		try {
			JSONArray  jsonObject = (JSONArray ) parser.parse(new FileReader("src\\main\\java\\com\\ebcon\\punchlogictest\\employeeData.json"));
			for(Object o : jsonObject) {
				OutputModel op = new OutputModel();
				ArrayList<TimePunch> punchTime = new ArrayList<TimePunch>();
				JSONObject jobdata = (JSONObject) o;
				String name = (String) jobdata.get("employee");
				op.setEmployeee(name);
				JSONArray punchArray = (JSONArray )  jobdata.get("timePunch");
				for(Object punchObject : punchArray) {
					JSONObject punchData = (JSONObject) punchObject;
					String jobName = (String) punchData.get("job");
					String startDate =  (String) punchData.get("start");
					String endDate = (String) punchData.get("end");
					Date start =  dateFormatter.parse(startDate);
					Date end =  dateFormatter.parse(endDate);
					double hours =  hoursWorkedPerShift(start, end) + remainingMinuteInHour(start, end);
					double currHour =  (hours + op.getRegular() + op.getOvertime() + op.getDoubletime());
					
					// set time;
					if(currHour <= 40) {
						op.setRegular(currHour);
					}else if(currHour > 40 && currHour <=48) {
						op.setOvertime(currHour - 40);
						op.setRegular(40);
					}else if(currHour > 48) {
						op.setRegular(40);
						op.setOvertime(8);
						op.setDoubletime(currHour - 48);
					}
					
					// perform calculation based on job type
					if(jobName.equals("Hospital - Painter")) {
						
						if(currHour <= 40 ) {
							double regularRateWage = hours * jobMap.get("Hospital - Painter").getRate();
							double total = op.getWageTotal() + regularRateWage;
							op.setWageTotal(total);
						}else if(currHour > 40 && currHour <=48) {
							double otRateWage = (currHour - 40) * (jobMap.get("Hospital - Painter").getRate() * 1.5);
							double tempRegRateHour = currHour - 40 < hours ? hours - (currHour - 40) : 0;
							double regularRateWage = tempRegRateHour * 31.25;
							double total = op.getWageTotal() + regularRateWage + otRateWage;
							op.setWageTotal(total);
						} else if(currHour > 48) {
							double dtRateWage = (currHour - 48) * (jobMap.get("Hospital - Painter").getRate() * 2);
							double tempOTRateHour = currHour - 48 < hours ? hours - (currHour - 48) : 0;
							double otRateWage =0;
							double regularRateWage =0;
							if(tempOTRateHour <= 8) {
								otRateWage = tempOTRateHour * (jobMap.get("Hospital - Painter").getRate() * 1.5);
							}else {
								otRateWage = 8 * (jobMap.get("Hospital - Painter").getRate() * 1.5);
								regularRateWage = (tempOTRateHour - 8 ) * jobMap.get("Hospital - Painter").getRate();
							}
							
							double total = op.getWageTotal() + regularRateWage + otRateWage + dtRateWage;
							op.setWageTotal(total);
						}
						
						//benefit for hospital painter for the shift
						double benefit = op.getBenefitTotal() + (hours * jobMap.get("Hospital - Painter").getBenefitsRate());
						op.setBenefitTotal(benefit);
					}else if(jobName.equals("Hospital - Laborer")) {
						if(currHour <= 40 ) {
							double regularRateWage = hours * jobMap.get("Hospital - Laborer").getRate();
							double total = op.getWageTotal() + regularRateWage;
							op.setWageTotal(total);
						}else if(currHour > 40 && currHour <=48) {
							double otRateWage = (currHour - 40) * (jobMap.get("Hospital - Laborer").getRate() * 1.5);
							double tempRegRateHour = currHour - 40 < hours ? hours - (currHour - 40) : 0;
							double regularRateWage = tempRegRateHour * jobMap.get("Hospital - Laborer").getRate();
							double total = op.getWageTotal() + regularRateWage + otRateWage;
							op.setWageTotal(total);
						} else if(currHour > 48) {
							double dtRateWage = (currHour - 48) * (jobMap.get("Hospital - Laborer").getRate() * 2);
							double tempOTRateHour = currHour - 48 < hours ? hours - (currHour - 48) : 0;
							double otRateWage =0;
							double regularRateWage =0;
							if(tempOTRateHour <= 8) {
								otRateWage = tempOTRateHour * (jobMap.get("Hospital - Laborer").getRate() * 1.5);
							}else {
								otRateWage = 8 * (jobMap.get("Hospital - Laborer").getRate() * 1.5);
								regularRateWage = (tempOTRateHour - 8 ) * jobMap.get("Hospital - Laborer").getRate();
							}
							
							double total = op.getWageTotal() + regularRateWage + otRateWage + dtRateWage;
							op.setWageTotal(total);
						}
						
						//benefit for hospital laborer for the shift
						double benefit = op.getBenefitTotal() + (hours * jobMap.get("Hospital - Laborer").getBenefitsRate());
						op.setBenefitTotal(benefit);
					} else if (jobName.equals("Shop - Laborer")) {
						if(currHour <= 40 ) {
							double regularRateWage = hours * jobMap.get("Shop - Laborer").getRate();
							double total = op.getWageTotal() + regularRateWage;
							op.setWageTotal(total);
						}else if(currHour > 40 && currHour <=48) {
							double otRateWage = (currHour - 40) * (jobMap.get("Shop - Laborer").getRate() * 1.5);
							double tempRegRateHour = currHour - 40 < hours ? hours - (currHour - 40) : 0;
							double regularRateWage = tempRegRateHour * jobMap.get("Shop - Laborer").getRate();
							double total = op.getWageTotal() + regularRateWage + otRateWage;
							op.setWageTotal(total);
						} else if(currHour > 48) {
							double dtRateWage = (currHour - 48) * (jobMap.get("Shop - Laborer").getRate() * 2);
							double tempOTRateHour = currHour - 48 < hours ? hours - (currHour - 48) : 0;
							double otRateWage =0;
							double regularRateWage =0;
							if(tempOTRateHour <= 8) {
								otRateWage = tempOTRateHour * (jobMap.get("Shop - Laborer").getRate()* 1.5);
							}else {
								otRateWage = 8 * (jobMap.get("Shop - Laborer").getRate() * 1.5);
								regularRateWage = (tempOTRateHour - 8 ) * jobMap.get("Shop - Laborer").getRate();
							}
							
							double total = op.getWageTotal() + regularRateWage + otRateWage + dtRateWage;
							op.setWageTotal(total);
						}
						
						//benefit for shop laborer for the shift
						double benefit = op.getBenefitTotal() + (hours * jobMap.get("Shop - Laborer").getBenefitsRate());
						op.setBenefitTotal(benefit);
					}
					
				}
				
				// round the results to 4 digit decimal
				op.setBenefitTotal(round(op.getBenefitTotal()));
				op.setDoubletime(round(op.getDoubletime()));
				op.setOvertime(round(op.getOvertime()));
				op.setWageTotal(round(op.getWageTotal()));
				op.setRegular(round(op.getRegular()));
				
				//convert to json object and add to to map
				JSONObject jOb = convertToJson(op);
				employeeDataMap.put(name, jOb);
			
			}
			return employeeDataMap;
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	// hours worked in a shift
	public static long hoursWorkedPerShift(Date start, Date end) {
		long shiftInMilisecond = end.getTime() - start.getTime();
		long hoursWorked = shiftInMilisecond / (1000 * 60 * 60) % 24;
		return hoursWorked;
	}
	
	// remaining minutes worked interms of hour.
	public static double remainingMinuteInHour(Date start, Date end) {
		double shiftInMilisecond = end.getTime() - start.getTime();
		double remainingMinutes = shiftInMilisecond / (1000 * 60 ) % 60;
		return (remainingMinutes * 1)/60;
	}
	
	// round to four decimal place
	public static double round(double val) {
		double result = val * 10000;
		result = Math.floor(result);
		result = result/10000;
		return result;
	}
	
	// convert output object to json
	public static JSONObject convertToJson(OutputModel output) {
		JSONObject jObject = new JSONObject();
		jObject.put("employee", output.getEmployeee());
		jObject.put("regular", output.getRegular());
		jObject.put("overtime", output.getOvertime());
		jObject.put("doubletime", output.getDoubletime());
		jObject.put("wageTotal", output.getWageTotal());
		jObject.put("benefitTotal", output.getBenefitTotal());
		return jObject;
	}
	
	//convert map to json
	public static JSONObject convertMapToJson(Map<String, JSONObject> employeeDataMap) {
		JSONObject jo = new JSONObject(employeeDataMap);	
		return jo;
	}
}
