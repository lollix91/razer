
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ToLaunch {

	public ToLaunch() {
		
		
		
	}
	
	
	private class ProjectEnd {
		public ProjectEnd() {
			
			
		}
		
		int providerId;
		int regionId;
		int numberTaken;
		
	}
	
	private class Project {
		
		public Project() {
			allocHistory = new ArrayList<ToLaunch.ProjectEnd>();
		}
		
		int penalty;
		String country;
		int countryIndex;
		int[] unitNeeded;

		int[] unitNeededAllocated;
		List<ProjectEnd> allocHistory;
	}

	
	private class Provider {
		
		public Provider() {
			
		}
		
		String name;
		int n_regions;
		Region[] regions;
		
	}
	
	private class Region {
		
		public Region() {
			
		}
		
		String name;
		int n_serviceunit;
		float packageCost;
		int[] unitsNumber;	
		int[] latencyWithCountry;
		
	}
	
	static Scanner in;
	static int n_providers;
	static int n_services;
	static int n_countries;
	static int n_projects;
	
	static String[] serviceNames;
	static String[] countriesNames;
	
	static Provider[] providers;
	static Project[] projects;
	
	public static void main(String[] args) throws FileNotFoundException {
		 
		ToLaunch a = new ToLaunch();
		
		in = new Scanner(new FileReader("first_adventure.in"));
		int i=0;
		int j=0;
		int z=0;
		
		while (in.hasNext()) { 
			
			if(i==0)
				n_providers=in.nextInt();
			else if(i==1)
				n_services=in.nextInt();				
			else if(i==2)
				n_countries=in.nextInt();				
			else if(i==3)
				n_projects=in.nextInt();				
			else if(i==4) {
				break;
			}
				
			i++;
		}
		
		serviceNames = new String[n_services];
		for(i=0; i<n_services; i++)
			serviceNames[i] = in.next();
		
		countriesNames = new String[n_countries];
		for(i=0; i<n_countries; i++)
			countriesNames[i] = in.next();
		
		providers = new Provider[n_providers];
		//scans the providers! main loop
		for(i=0; i<n_providers; i++) {
			providers[i] = a.new Provider();
					
			providers[i].name = in.next();
			providers[i].n_regions = in.nextInt();
			
			providers[i].regions = new Region[providers[i].n_regions];
			
			for(j=0; j<providers[i].n_regions; j++) {
				
				providers[i].regions[j] = a.new Region();
				
				providers[i].regions[j].name = in.next();
				providers[i].regions[j].n_serviceunit = in.nextInt();
				providers[i].regions[j].packageCost = Float.parseFloat(in.next());
				
				providers[i].regions[j].unitsNumber = new int[n_services];
				for(z=0; z<n_services; z++) {
					providers[i].regions[j].unitsNumber[z] = in.nextInt();
				}
				
				providers[i].regions[j].latencyWithCountry = new int[n_countries];
				for(z=0; z<n_countries; z++) {
					providers[i].regions[j].latencyWithCountry[z] = in.nextInt();
					
				}
				
			}
			
			
		}
		
		projects = new Project[n_projects];
		for(i=0; i<n_projects; i++) {
			
			projects[i] = a.new Project();
			
			projects[i].penalty = in.nextInt();
			projects[i].country = in.next();
			
			for(int h = 0; h<n_countries; h++) {
				if(countriesNames[h].equals(projects[i].country)) {
					projects[i].countryIndex = h;
					break;
				}
			}
			
			
			
			
			projects[i].unitNeeded = new int[n_services];
			projects[i].unitNeededAllocated = new int[n_services];

			for(j=0; j<n_services; j++) {
				
				projects[i].unitNeeded[j] = in.nextInt();
				projects[i].unitNeededAllocated[j] = 0;
				
			}
			
		}

		
	
	//FINE IMPORT
		
		for(i = 0; i<n_projects; i++) {
			

			
			int[][] toSkip = new int[n_providers][];
			for(int gg = 0; gg<n_providers; gg++) {
				toSkip[gg] = new int[providers[gg].n_regions];
				for(int ggi = 0; ggi < providers[gg].n_regions; ggi++)
					toSkip[gg][ggi] = 0;
			}
			
			int iterN = 0;
			
			while(true) {
				
				int minLat = Integer.MAX_VALUE;
				int minIndexProv = 0;
				int minIndexReg = 0;
				
				iterN+=1;
				
				
				for(j=0; j<n_providers; j++) {
					for(int n = 0; n < providers[j].n_regions; n++) {
						
						
						
						if(toSkip[j][n] == 1)
							continue;
						
						int ml = providers[j].regions[n].latencyWithCountry[projects[i].countryIndex];
						if(ml<minLat) {
							minLat = ml;
							minIndexProv = j;
							minIndexReg = n;
							

						}
						
					}
				}

				
				//take resources from provider and region minIndexProv and minIndexReg
				int packetsToBuy = 0;
				while(true) {
					
					packetsToBuy += 1;
					boolean toContinue = false;
					for(j=0; j<n_services; j++) {
						projects[i].unitNeededAllocated[j] += providers[minIndexProv].regions[minIndexReg].unitsNumber[j];
						

						
						if(providers[minIndexProv].regions[minIndexReg].unitsNumber[j] > 0) {
							if(projects[i].unitNeededAllocated[j] < projects[i].unitNeeded[j]) {
								toContinue = true;
							}
						}
					
					}

					
					if(toContinue == false) {
						ProjectEnd pp = a.new ProjectEnd();
						pp.numberTaken = packetsToBuy;
						pp.providerId = minIndexProv;
						pp.regionId = minIndexReg;
						projects[i].allocHistory.add(pp);
					
						toSkip[minIndexProv][minIndexReg] = 1;
						
						break;
					}
					
					
				}
				
				
				boolean toContinueA = false;
				for(int yy = 0; yy<n_services; yy++) {
					if(projects[i].unitNeededAllocated[yy] < projects[i].unitNeeded[yy]) {
						toContinueA = true;
					}					
				}
				
				if(toContinueA == false) {
					break;
				}

				
				
			
			}
			

		}
		
		
		
		
		
		
		//writedata
		
		try{
		    PrintWriter writer = new PrintWriter("output.txt", "UTF-8");
		    
		    for(int b = 0; b<n_projects; b++) {
		    	for(int gg = 0; gg<projects[b].allocHistory.size(); gg++) {
				    writer.print(projects[b].allocHistory.get(gg).providerId+" ");
				    writer.print(projects[b].allocHistory.get(gg).regionId+" ");

				    if(gg == projects[b].allocHistory.size()-1)
				    	writer.print(projects[b].allocHistory.get(gg).numberTaken);
				    else
					    writer.print(projects[b].allocHistory.get(gg).numberTaken+" ");


		    	}
		    	writer.println();
		    	
		    }

		    writer.close();

		    
		    
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		
		
		
		
		
		
		
		
	}
	
		/*
		
		//fine import!!
		
		List<Map<Integer, Integer>> caches_videos_weight = new ArrayList<Map<Integer, Integer>>();
		
		
		for(i=0; i<n_caches; i++) {
		    Map<Integer, Integer> videos_weight = new HashMap<Integer, Integer>();
		
			
			for(j=0; j<n_endpoints; j++) {
				
				if(endpoint_caches_latency[j][i]!=0) {//è collegato
					for(z=0; z<n_videos; z++) {
						if(endpoint_videos_request[j][z]!=0) {//ho le richieste da quell'endpoint
							//inserisco il video e il peso da quell endpoint (ossia peso_endpointdc-peso_endpoint_cache)
							
							//peso del video dal datacenter
							int temp=datacenter_latency[j]*endpoint_videos_request[j][z];
							int temp1=temp-(endpoint_videos_request[j][z]*endpoint_caches_latency[j][i]);
							
							//se è gia presente un indice z, metto il piu grande!
							if((videos_weight.get(z)!=null)&&(videos_weight.get(z)>temp1)) { 
								videos_weight.put(z, videos_weight.get(z));
							}
							else
								videos_weight.put(z, temp1);

						}
						
					}


					
					
					
					
				}
				

				
			}

			caches_videos_weight.add(videos_weight);

			
			
		}
		
		//adesso in caches_videos_weight ho i pesi e i video richiesti 
		for(i=0; i<n_caches; i++) {
			
			Map<Integer, Integer> sortedMap = 
					caches_videos_weight.get(i).entrySet().stream()
				    .sorted(Entry.comparingByValue())
				    .collect(Collectors.toMap(Entry::getKey, Entry::getValue,
				                              (e1, e2) -> e1, LinkedHashMap::new));
			
			
			caches_videos_weight.set(i, sortedMap);
			
		}
		
		
		try{
		    PrintWriter writer = new PrintWriter("output.txt", "UTF-8");
		    writer.println(n_caches);

			List<Integer> video_to_put= new ArrayList<>();
			for(i=0; i<n_videos; i++) {
				video_to_put.add(i);
			}
		    
		for(i=0; i<n_caches; i++) {
		
		int actual_size=caches_size;
		List<Integer> cache_video_end = new ArrayList<>();
		
		for(j=0; j<n_videos; j++)
				if(caches_videos_weight.get(i).get(j)!=null) {
					if(actual_size-videos_size[j]>=0) {
						cache_video_end.add(j);
						actual_size=actual_size-videos_size[j];
					}
			}
			
	    writer.print(i);

		for(Integer aa : cache_video_end) {
		    writer.print(" "+aa);
			
		}

		writer.println();
		
		}
		
		
		
	    writer.close();
	} catch (IOException e) {
	   // do something
	}
	
		
		

	}
	
	
	*/


	
	
}
