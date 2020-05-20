public class test {

	public static void main(String[] args){
		int k = 0;
		int j = 2;
		for (int i = 1; i < 100; i = i + 2)
		{
			j = j - 1;
			k++;
			while(j < 15)
			{
				j = j + 5;
			}
		}
		System.out.println(k);
	}

}
