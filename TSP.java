import java.util.*;
import java.io.*;
public class TSP
{
	static int cost_matrix[][],adjacency_matrix[][],size;
	static Integer population[][];
	static int fitnessvalue[];
	static int selection_list[];
	static List list;
	static double average_fitness1=0.0;
	static double average_fitness2=0.0;
	static boolean flag=false;
	static double difference;
	public static void buildCostMatrix() throws Exception
	{
		
				String s;
		BufferedReader brf=new BufferedReader(new FileReader("d:\\edgelist1.txt"));
		size=Integer.parseInt(brf.readLine());
		if((size&1)==1)
			size++;
		 adjacency_matrix=new int[size+1][size+1];
		cost_matrix=new int[size+1][size+1];
		while((s=brf.readLine())!=null)
		{
			String value[]=s.split(" ");
			int i=Integer.parseInt(value[0]);
			int j=Integer.parseInt(value[1]);
			int cost=Integer.parseInt(value[2]);
			adjacency_matrix[i][j]=1;
			cost_matrix[i][j]=cost;
			cost_matrix[j][i]=cost;
		}
	}
	
	public static void rouletteWheelSelection()
	{
		selection_list=new int[size];
		list=new ArrayList();
		Random rn=new Random();
		for(int i=0;i<size;i++)
		{
			int x=rn.ints(1,size+1).findFirst().getAsInt();
			selection_list[i]=x;
			list.add(fitnessvalue[i]);
		}
	}
	
	public static void insertFittestChromosome()
	{
		Random rn=new Random();
		List list1=new ArrayList(list);
		Collections.sort(list);//sort the list for getting minimum element
		Integer minimum=(Integer)list.get(0);//getting fittest chromosome
		int x=rn.ints(1,size+1).findFirst().getAsInt();//getting random index for swapping
		int index=list1.indexOf(minimum);//for getting index of fittest chromosome
		for(int j=0;j<size;j++)
			population[x][j]=population[index][j];
		
	}
	
	public static void crossover()
	{
		for(int i=0;i<(size/2);i++)
		{
			int parent1=selection_list[i];
			int parent2=selection_list[size-(i+1)];
			for(int j=0;j<size;j++)
			{
				int temp=population[parent1][j];
				population[parent1][j]=population[parent2][size-(j+1)];
				population[parent2][size-(j+1)]=temp;
			}
		}
	}
	
	public static void mutation()
	{
		double mutation_probability=0.01;
		Random rn=new Random();
		int index=rn.ints(1,size).findFirst().getAsInt();
		int swap_index1=rn.ints(1,size).findFirst().getAsInt();
		int swap_index2=rn.ints(1,size).findFirst().getAsInt();
		while(swap_index1==swap_index2)
			swap_index2=rn.ints(1,size).findFirst().getAsInt();
		double probability=rn.ints(0,11).findFirst().getAsInt()*0.01;
		if(probability<=mutation_probability)
		{
			int temp=population[index][swap_index1];
			population[index][swap_index1]=population[index][swap_index2];
			population[index][swap_index2]=temp;
		}
	}
	
	public static void calculate_average_Fitness()
	{
		
		for(int i=0;i<size;i++)
			average_fitness2=average_fitness2+fitnessvalue[i];
		average_fitness2=average_fitness2/size;
	}
	
	public static void calculateFitness()
	{
		
		fitnessvalue=new int[size];
		for(int i=1;i<=size;i++)
			{
				int fitness=0;
				for(int j=0;j<size-1;j++)
				{
					fitness=fitness+cost_matrix[population[i][j]][population[i][j+1]];
				}
				fitnessvalue[i-1]=fitness;
			}
	}
	public static void buildPopulation()
	{
		if(flag)
			insertFittestChromosome();
		
		Random r=new Random();
		ArrayList<Integer> al;
		int population_size=size;
		population=new Integer[population_size+1][population_size+1];
		for(int i=1;i<=population_size;i++)
		{
			al=new ArrayList<Integer>();
			for(int j=1;j<=population_size;j++)
			{
				int x=r.ints(1,population_size+1).findFirst().getAsInt();
					while(al.contains(x))
						x=r.ints(1,population_size+1).findFirst().getAsInt();
					al.add(x);
			}
			population[i]=al.toArray(population[i]);
		}	
	}
	public static void showpopulation()
	{
		for(int i=1;i<=size;i++)
			{
				for(int j=0;j<size-1;j++)
				{
					System.out.printf("%3d -> ",population[i][j]);
				}
				System.out.printf("%3d  ::  ",population[i][size-1]);
				System.out.print(fitnessvalue[i-1]);
				System.out.println();
			}
	}
	public static void main(String args[]) throws Exception
	{
		for(int i=0;i<100;i++)
		{
			buildCostMatrix();
			do{
					average_fitness1=average_fitness2;
					buildPopulation();
					calculateFitness();
					rouletteWheelSelection();
					crossover();
					mutation();
					calculate_average_Fitness();
					flag=true;
					difference=Math.abs(average_fitness1-average_fitness2);
			}while(difference>=0.01);
		}
			showpopulation();
			
	}
}