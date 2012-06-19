package controllers;
 
import play.*;
import play.mvc.*;
 
import java.util.*;
import java.text.*;
 
import models.*;


 
@Check("admin")
@With(Secure.class)
public class Reports extends Controller {
    
	private static SimpleDateFormat dayformatter= new SimpleDateFormat("dd");
	private static SimpleDateFormat yearformatter= new SimpleDateFormat("yyyy");
	private static SimpleDateFormat timeformatter= new SimpleDateFormat("dd hh:mm");
	private static SimpleDateFormat dateformatter= new SimpleDateFormat("dd-MM-yyyy");
	private static SimpleDateFormat datetimeformatter= new SimpleDateFormat("dd-MM-yyyy");
	
    @Before
    static void setConnectedUser() {
        if(Security.isConnected()) {
            User user = User.find("byEmail", Security.connected()).first();
            renderArgs.put("user", user.toString());
        }
    }
 
    public static void index() {
		List<Duty> duties = Duty.findAll();
		List<User> users = User.listUsers();
		
        render(users, duties);
    }
	
	public static void dutyTotals() {
		int year = 0;
		long totalTime = 0;
		String totalTimeString = "";
		List<Time> times = Time.findAll();
		Iterator<Time> iteratorItems = times.iterator();
		HashMap dutyTotals = new HashMap();
		PieChart report = new PieChart("Totals per Category", 300, 1000, true);
		
		try {
			year = Integer.parseInt(params.get("year"));
		} catch(Exception e) {
			//throw new Exception("Error: Incorrect year entered, please go back to Reports page and select a year value.");
		}
		
		while(iteratorItems.hasNext()) {
			Time timeObject = iteratorItems.next();
			
			if(timeObject.Date != null && year == Integer.parseInt(yearformatter.format(timeObject.Date.getTime()))) {
				if(dutyTotals.containsKey(timeObject.Duty.Category.Name)) {
					long tempTimeMinutes = Long.parseLong(dutyTotals.get(timeObject.Duty.Category.Name).toString());
					tempTimeMinutes += timeObject.getTimeMinutes();
					
					dutyTotals.put(timeObject.Duty.Category.Name, tempTimeMinutes);
				} else {
					dutyTotals.put(timeObject.Duty.Category.Name, timeObject.getTimeMinutes());
				}
				
				totalTime += timeObject.getTimeMinutes();
			}
		}
		
		for(Object key: dutyTotals.keySet()) {
			double perc = (Double.parseDouble(dutyTotals.get(key).toString()) / (double)totalTime) * 100;
		
			report.addSlice(String.format("%s (%s)", key, dutyTotals.get(key).toString()), (int)perc);
		}
		
		totalTimeString = "" + totalTime;
		
		render(report, dutyTotals, totalTimeString, year);
	}
	
	public static void userlist() {
		//String graphType = params.get("report");
		List<User> users = User.findAll();
		
		//2 iterators, 1 for total, other to calc per item
		Iterator<User> iteratorTotals = users.iterator();
		Iterator<User> iteratorItems = users.iterator(); //not use in this instance
		
		int adminCount = 0;
		int staffCount = 0;
		int finalTotal = 0;
		
		
		//calc totals
		while (iteratorTotals.hasNext()) {
			User userObject = iteratorTotals.next();
			if(userObject.isAdmin) {
				adminCount++;
			} else {
				staffCount++;
			}
			
			finalTotal++;
        }
		
		//if(graphType.equalsIgnoreCase("PieChart")) {
			PieChart report = new PieChart("User Types", 200, 500, true);
			
			//add slices then generate, render
			if(finalTotal != 0) {
				double adminPerc = ((double)adminCount/(double)finalTotal)*100;
				double staffPerc = ((double)staffCount/(double)finalTotal)*100;
			
				report.addSlice(String.format("Administrators (%s)", adminCount), (int)adminPerc);
				report.addSlice(String.format("Staff (%s)", staffCount), (int)staffPerc);
				
				render(users, report);
			}
		//} else {
			//Barchart1 report = new Barchart1();
		//	render(users);
		//}
	}
	
	public static void staffMonthTotals() {
		long userId = Long.parseLong(params.get("user"));
		int year = Integer.parseInt(params.get("year"));
		SimpleDateFormat monthformatter= new SimpleDateFormat("M");
		
		//List<Time> times = Time.listBycategoryAndUser("%", userId);
		List<Time> times = Time.findAll();
		List<Duty> duties = Duty.findAll();//Duty.getForUser(userId);
		List<DutyCategory> dutyCategories = DutyCategory.findAll();
		
		ArrayList<Time> filteredTimes = new ArrayList<Time>();
		
		Iterator<Time> iteratorItems = times.iterator();
		
		while (iteratorItems.hasNext()) {
			Time timeObject = iteratorItems.next();
			
			if(timeObject.staff.id == userId && year == Integer.parseInt(yearformatter.format(timeObject.Date.getTime()))) {
				filteredTimes.add(timeObject);
			}
		}
		
		render(filteredTimes, dutyCategories, duties, year, monthformatter);
	}
	
	public static void staffDailyDetails() {
		long userId = Long.parseLong(params.get("user"));
		int year = Integer.parseInt(params.get("year"));
		int month = Integer.parseInt(params.get("month"));
		SimpleDateFormat monthformatter= new SimpleDateFormat("M");
		
		//List<Time> times = Time.listBycategoryAndUser("%", userId);
		List<Time> times = Time.findAll();
		List<Duty> duties = Duty.findAll();
		List<DutyCategory> dutyCategories = DutyCategory.findAll();
		
		ArrayList<Time> filteredTimes = new ArrayList<Time>();
		
		Iterator<Time> iteratorItems = times.iterator();
		
		while (iteratorItems.hasNext()) {
			Time timeObject = iteratorItems.next();
			
			if(timeObject.staff.id == userId && year == Integer.parseInt(yearformatter.format(timeObject.Date.getTime()))
				&& month == Integer.parseInt(monthformatter.format(timeObject.Date.getTime()))) {
				filteredTimes.add(timeObject);
			}
		}
		
		render(filteredTimes, dutyCategories, duties, year, month, dateformatter, datetimeformatter);
	}
	
	public static void generalStatistics() {
		int year = Integer.parseInt(params.get("year"));
		int month = Integer.parseInt(params.get("month"));
		SimpleDateFormat monthoutputformatter= new SimpleDateFormat("MMMM");
		SimpleDateFormat monthformatter = new SimpleDateFormat("M");
		
		//List<Time> times = Time.listBycategoryAndUser("%", userId);
		List<Time> times = Time.findAll();
		List<Duty> duties = Duty.findAll();
		List<DutyCategory> dutyCategories = DutyCategory.findAll();
		List<User> users = User.findAll();
		
		ArrayList<Time> filteredTimes = new ArrayList<Time>();
		ArrayList<String> outputLines = new ArrayList<String>();
		
		HashMap<Long, Integer> usersMap = new HashMap<Long, Integer>();
		HashMap<String, Integer> dutiesMap = new HashMap<String, Integer>();
		HashMap<String, Integer> monthsMap = new HashMap<String, Integer>();
		
		Iterator<Time> iteratorItems = times.iterator();
		Iterator<User> iteratorUsers = users.iterator();
		Iterator<Time> iteratorItemsUsers = times.iterator();
		Iterator<Time> iteratorItemsDuties = times.iterator();
		Iterator<Time> iteratorItemsMonths = times.iterator();
		
		Long topUserId = 0l;
		Integer topUserAmount = 0;
		Integer topDutyAmount = 0;
		Integer topMonthAmount = 0;
		String topUser = "";
		String topDuty = "";
		String topMonth = "";
		String currentPeriod = "";
		
		while(iteratorItemsUsers.hasNext()) {
			Time timeObject = iteratorItemsUsers.next();
			
			if(month == Integer.parseInt(monthformatter.format(timeObject.Date.getTime()))
				&& year == Integer.parseInt(yearformatter.format(timeObject.Date.getTime()))) {
				if(usersMap.containsKey(timeObject.staff.id)) {
					Integer timeHolder = usersMap.get(timeObject.staff.id);
					
					timeHolder += timeObject.getTimeMinutes();
					usersMap.put(timeObject.staff.id, timeHolder);
				} else {
					usersMap.put(timeObject.staff.id, timeObject.getTimeMinutes());
				}
			}
		}
		
		while(iteratorItemsDuties.hasNext()) {
			Time timeObject = iteratorItemsDuties.next();
			String keyName = "" + timeObject.Duty.Name + " (" + timeObject.Duty.Category.Name + ")";
			
			if(month == Integer.parseInt(monthformatter.format(timeObject.Date.getTime()))
				&& year == Integer.parseInt(yearformatter.format(timeObject.Date.getTime()))) {
				
				if(dutiesMap.containsKey(keyName)) {
					Integer timeHolder = dutiesMap.get(keyName);
					
					if (timeHolder == null)  
						timeHolder = 0;
					
					timeHolder += timeObject.getTimeMinutes();
					
					dutiesMap.put(keyName, timeHolder);
					
				} else {
					dutiesMap.put(keyName, timeObject.getTimeMinutes());
				}
			}
		}
		
		while(iteratorItemsMonths.hasNext()) {
			Time timeObject = iteratorItemsMonths.next();
			
			if(year == Integer.parseInt(yearformatter.format(timeObject.Date.getTime()))) {
				if(monthsMap.containsKey(monthoutputformatter.format(timeObject.Date.getTime()))) {
					Integer timeHolder = monthsMap.get(monthoutputformatter.format(timeObject.Date.getTime()));
					
					if (timeHolder == null)  
						timeHolder = 0;
					
					timeHolder += timeObject.getTimeMinutes();
					
					monthsMap.put(monthoutputformatter.format(timeObject.Date.getTime()), timeHolder);
				} else {
					monthsMap.put(monthoutputformatter.format(timeObject.Date.getTime()), timeObject.getTimeMinutes());
				}
			}
		}
		
		for(Long id : usersMap.keySet()) {
			if(usersMap.get(id) >= topUserAmount) {
				topUserId = id;
				topUserAmount = usersMap.get(id);
			}
		}
		
		for(String key : dutiesMap.keySet()) {
			if(dutiesMap.get(key) >= topDutyAmount) {
				topDuty = key;
				topDutyAmount = dutiesMap.get(key);
			}
		}
		
		for(String key : monthsMap.keySet()) {
			if(monthsMap.get(key) >= topMonthAmount) {
				topMonth = key;
				topMonthAmount = monthsMap.get(key);
			}
		}
		
		while (iteratorUsers.hasNext()) {
			User userObject = iteratorUsers.next();
			
			if(userObject.id == topUserId) {
				topUser = userObject.toString();
			}
		}
		
		if(topUserAmount != 0)
			outputLines.add(String.format("Staff member \"%s\" has spend the most time this month on the system (%s minutes)", topUser, topUserAmount));
			
		if(topDutyAmount != 0)
			outputLines.add(String.format("The duty \"%s\" has the most time scored against this month on the system (%s minutes)", topDuty, topDutyAmount));
			
		if(topMonthAmount != 0)
			outputLines.add(String.format("The month of %s has the most time scored against it for the year (%s minutes)", topMonth, topMonthAmount));
		
		render(outputLines, year, month);
	}
	
	public static void dutyStatistics() {
		int year = Integer.parseInt(params.get("year"));
		int month = Integer.parseInt(params.get("month"));
		SimpleDateFormat monthformatter= new SimpleDateFormat("M");
		
		//List<Time> times = Time.listBycategoryAndUser("%", userId);
		List<Time> times = Time.findAll();
		List<Duty> duties = Duty.findAll();
		List<DutyCategory> dutyCategories = DutyCategory.findAll();
		
		ArrayList<Time> filteredTimes = new ArrayList<Time>();
		ArrayList<models.PieChart> reports = new ArrayList<models.PieChart>();
		HashMap<String, HashMap<String, Integer>> dutiesMap = new HashMap<String, HashMap<String, Integer>>();
		
		Iterator<Time> iteratorItems = times.iterator();
		
		while (iteratorItems.hasNext()) {
			Time timeObject = iteratorItems.next();
			
			if(year == Integer.parseInt(yearformatter.format(timeObject.Date.getTime()))
				&& month == Integer.parseInt(monthformatter.format(timeObject.Date.getTime()))) {
				filteredTimes.add(timeObject);
				
				if(dutiesMap.containsKey(timeObject.Duty.Category.Name)) {
					HashMap<String, Integer> valuesMap = dutiesMap.get(timeObject.Duty.Category.Name);
					
					if(valuesMap.containsKey(timeObject.Duty.Name)) {
						Integer timeHolder = valuesMap.get(timeObject.Duty.Name);
						timeHolder += timeObject.getTimeMinutes();
						
						valuesMap.put(timeObject.Duty.Name, timeHolder);
					} else {
						valuesMap.put(timeObject.Duty.Name, timeObject.getTimeMinutes());
					}
					
					dutiesMap.put(timeObject.Duty.Category.Name, valuesMap);
				} else {
					//can create all
					HashMap<String, Integer> valuesMap = new HashMap<String, Integer>();
					
					valuesMap.put(timeObject.Duty.Name, timeObject.getTimeMinutes());
					dutiesMap.put(timeObject.Duty.Category.Name, valuesMap);
				}
			}
		}
		
		for(String key : dutiesMap.keySet()) {
			models.PieChart report = new models.PieChart(key, 300, 1000, true);
			HashMap<String, Integer> valuesMap = dutiesMap.get(key);
			int total = 0;
			
			//get total
			for(String key2 : valuesMap.keySet()) {
				total += valuesMap.get(key2);
			}
			
			//build slices
			for(String key2 : valuesMap.keySet()) {
				double perc = ((double)valuesMap.get(key2) / (double)total) * 100;
				
				report.addSlice(String.format("%s (%s)", key2, valuesMap.get(key2)), (int)perc);
			}
			
			reports.add(report);
		}
		
		render(reports, year, month);
	}
	
	public static void staffAverages() {
		long userId = Long.parseLong(params.get("user"));
		String staffMember = "";
		int year = Integer.parseInt(params.get("year"));
		SimpleDateFormat monthformatter= new SimpleDateFormat("M");
		HashMap<String, Integer[]> itemsMap = new HashMap();
		HashMap<String, Integer[]> countMap = new HashMap();
		
		//List<Time> times = Time.listBycategoryAndUser("%", userId);
		List<Time> times = Time.findAll();
		List<Duty> duties = Duty.findAll();
		List<DutyCategory> dutyCategories = DutyCategory.findAll();
		
		ArrayList<Time> filteredTimes = new ArrayList<Time>();
		
		Iterator<Time> iteratorItems = times.iterator();	
		Iterator<Duty> iteratorDuties = duties.iterator();
		
		while (iteratorItems.hasNext()) {
			models.Time timeObject = iteratorItems.next();
			
			if(timeObject.staff.id == userId && year == Integer.parseInt(yearformatter.format(timeObject.Date.getTime()))) {
				filteredTimes.add(timeObject);
				
				if(staffMember.length() <= 0)
					staffMember = timeObject.staff.toString();
				
				int currentMonth = Integer.parseInt(monthformatter.format(timeObject.Date.getTime()));
				
				//fill maps for report
				if(itemsMap.containsKey(timeObject.Duty.Category.Name)) {
					Integer[] array = itemsMap.get(timeObject.Duty.Category.Name);
					Integer[] arrayTots = countMap.get(timeObject.Duty.Category.Name);
			
					if(timeObject.getTimeMinutes() > 0) {
						array[currentMonth-1] += timeObject.getTimeMinutes();
						arrayTots[currentMonth-1]++;
					}
					
					itemsMap.put(timeObject.Duty.Category.Name, array);
					countMap.put(timeObject.Duty.Category.Name, arrayTots);
				
				} else {
					Integer[] array = new Integer[12];
					Integer[] arrayTots = new Integer[12];
					
					for(int i = 0; i < 12; i++) {
						array[i] = 0;
						arrayTots[i] = 0;
					}
					
					if(timeObject.getTimeMinutes() > 0) {
						array[currentMonth-1] += timeObject.getTimeMinutes();
						arrayTots[currentMonth-1]++;
					}
					
					itemsMap.put(timeObject.Duty.Category.Name, array);
					countMap.put(timeObject.Duty.Category.Name, arrayTots);
				}
			}
		}
		
		if(filteredTimes.size() > 0) {
			models.LineChart report = new models.LineChart("Averages", 300, 1000);
			
			//create a line for every duty category
			for(String key : itemsMap.keySet()) {
				ArrayList<Double> list = new ArrayList<Double>();
				Integer[] itemsHolder = itemsMap.get(key);
				Integer[] countHolder = countMap.get(key);
				
				for(int i = 0; i < itemsHolder.length; i++) {
					double calc = 0;
					
					if(countHolder[i] != 0)
						calc = (double)itemsHolder[i] / (double)countHolder[i];
						
					list.add(calc);
				}
				
				report.addLine(key, list);
			}
			
			//add x axis labels
			report.addXAxisLabelsNumeric(1, 12, 1);
			report.addXAxisLabel("Months", 6);
			
			render(report, year, staffMember);
		}
		
		models.LineChart report = new models.LineChart("", 1, 1);
		
		render(report, year, staffMember);
	}
}
