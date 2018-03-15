
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
	
	static Scanner in;
	static int n_videos;
	static int n_endpoints;
	static int n_request;
	static int n_caches;
	static int caches_size;
	
	static int videos_size[];
	static int datacenter_latency[];
	static int endpoint_caches_latency[][];
	static int endpoint_videos_request[][];
	
	public static void main(String[] args) throws FileNotFoundException {
		 
		in = new Scanner(new FileReader("trending_today.in"));
		int i=0;
		int j=0;
		int z=0;
		
		while (in.hasNext()) { 
			
			if(i==0)
				n_videos=in.nextInt();
			else if(i==1)
				n_endpoints=in.nextInt();				
			else if(i==2)
				n_request=in.nextInt();				
			else if(i==3)
				n_caches=in.nextInt();				
			else if(i==4) {
				caches_size=in.nextInt();
				break;
			}
				
			i++;
		}
		
		videos_size=new int[n_videos];
		
		for(i=0; i<n_videos; i++)
			videos_size[i]=in.nextInt();

		datacenter_latency=new int[n_endpoints];
		endpoint_caches_latency=new int[n_endpoints][n_caches];
		
		for(i=0; i<n_endpoints; i++) {
			datacenter_latency[i]=in.nextInt();
			
			int temp1=in.nextInt();
			
			for(j=0; j<temp1; j++) {
				endpoint_caches_latency[i][in.nextInt()]=in.nextInt();
			}

			
		}
		
		endpoint_videos_request=new int[n_endpoints][n_videos];
		
		for(i=0; i<n_request; i++) {
			int temp1=in.nextInt();
			endpoint_videos_request[in.nextInt()][temp1]=in.nextInt();
			
		}
		
		
		
		
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
	
	
	


	
	
}
