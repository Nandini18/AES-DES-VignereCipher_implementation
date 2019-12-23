import java.io.*;
import java.util.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
public class q7vigenere{

static char[] alphabets=new char[]{'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
static double englishfreq[]=new double[]{0.08167,0.01492,0.02782,0.04253,0.12702,0.02228,0.02015,0.06094,0.06966,0.00153,0.00772,
0.04025,0.02406,0.06749,0.07507,0.01929,0.00095,0.05987,0.06327,0.09056,0.02758,0.00978,
0.02360,0.00150,0.01974,0.00074};
static int alphabet_numeric[]=new int[26];
static String cipher="";

public static void main(String args[]) throws Exception  //reads the file from the folder and calls method get_key_length
{	
	//File file = new File("C:\\Users\\Ananya\\AppData\\Local\\Packages\\CanonicalGroupLimited.UbuntuonWindows_79rhkp1fndgsc\\LocalState\\rootfs\\home\\nandini\\csp\\7c.txt"); 
  	//BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Ananya\\AppData\\Local\\Packages\\CanonicalGroupLimited.UbuntuonWindows_79rhkp1fndgsc\\LocalState\\rootfs\\home\\nandini\\csp\\7c.txt")); 
  	BufferedReader brf=new BufferedReader(new InputStreamReader(System.in));
  	System.out.println("Enter the cipher text:");
  	cipher = brf.readLine();
	get_key_length(cipher);
}

public static void get_key_length(String c) //accepts the cipher text from the main method and finds the key length using the Index of Coincidence 
{
	int i=0;
	int j=0;
	int k=0;
	int l=0;
	int freq=0;
	double max=0.0;
	double f=0.0;
	double c2=0.0;
	int key_len=0;
	int cipher_length=c.length(); //length of the whole file
	String set="";
	char x;
	int ch=0;
	for(i=0;i<26;i++)
	{
		alphabet_numeric[i]=0;  //array to manage frequency of letters in the cipher text
	}
	double ioc[]=new double[7]; //array to store the index of coincidence for every key length
	for(i=0;i<7;i++)
	{
		ioc[i]=0;
	}
	double coincidence=0.0;
	for(i=1;i<=6;i++)
	{
		c2=0.0;
		for(j=0;j<i;j++)
		{
			set=getset(j,i,c);			//breaking the whole file into sets using getset method depending on the key i
			for(k=0;k<set.length();k++)
			{
				x=set.charAt(k);		//scanning through each set and finding the frequency of the letters in the set
				ch=(int)x-'a';
				if(ch<0)
					ch=ch+32;
				ch=ch%26;				//getting the value of that character in the modulo 26 world
				freq=checkfreq(x,set);	//calling method checkfreq to calculate frequency of each character in the set
				alphabet_numeric[ch]=freq;	//storing that frequency for that set that key in this array.
			}
			for(l=0;l<26;l++)
			{
					f=f+(alphabet_numeric[l]*(alphabet_numeric[l]-1));   
					alphabet_numeric[l]=0;
			}
			coincidence=f/(set.length()*(set.length()-1));	//using index of coincidence formula to calculate the coincidence for each set each key
			f=0.0;
			c2=c2+(coincidence/i);		//adding the coincidences of all sets of the same key and taking out the average.
			coincidence=0.0;

		}
		
		ioc[i]=c2; //storing the coincidences of each key in this array

	}
	max=ioc[0];
	for(i=1;i<=6;i++)   	//finding the maximum index of coincidence, which ever one is maximum the index of that becomes key length
	{	
		if(ioc[i]>max)
		{
			max=ioc[i];
			key_len=i;
		}
	}
	
	System.out.println("Key length is: " + key_len); //printing key length
	get_key(key_len,c); //calling get_key to find out the key needed for decryption
}

public static void get_key(int k,String s)	//method which finds the key using the chi-square statistic
{
	int i=0;
	int j=0;
	int z=0;
	int m=0;
	double min=0.0;
	int a=0;
	double sum=0.0;
	String key="";
	String group="";
	double chi[]=new double[26];	//to store the chi square for each letter of a set
	String sets[]=new String[k];	//array to store all sets
	for(i=0;i<k;i++)
	{
		sets[i]=getset(i,k,s);	//breaking the whole cipher text(original) again on the basis of the key length this time
	}


	for(j=0;j<k;j++)
	{
		group = sets[j];
		for(i=0;i<26;i++)
		{
			for(z=0;z<26;z++)
			{
				alphabet_numeric[z]=0;		//making our frequency array to again be 0 to prevent using old values
			}
			rotate(group,i); //calling the method rotate to shift each set in all possible 26 keys
			for(m=0;m<26;m++)
			{
				sum=sum+Math.pow((alphabet_numeric[m]-group.length()*englishfreq[m]),2)/(group.length()*englishfreq[m]); //using chi-square formula on each letter of a particular set j for a particular shift (i)
			}
			chi[i]=sum;//storing the sum of chi square values for all letters in a set j for a particular shift i
			sum=0.0;
		}

		min=chi[0];
		a=0;
		for(m=0;m<26;m++)		//finding the minimum chi value for that set, whichever index has the minimal value, the value at that index in the alphabets array will be in the key
		{
			if(chi[m]<min)
			{
				min=chi[m];
				a=m;
			}
		}

		key=key+alphabets[a]; //generating the key
	}
	System.out.println(key);
	decrypt(key,k); //once key is found calling decrypt
	
}

public static int checkfreq(char c,String check)	//method to find the frequency of letters in a string
{
	int i=0;
	int count=0;
	for(i=0;i<check.length();i++)
	{
		if(check.charAt(i)==c)
		{
			count++;
		}
	}

	return count;
	
}

public static String getset(int start,int next,String text)  //method to break the whole text into sets depending on the key
{
	int i=0;
	String fset="";
	for(i=start;i<text.length();i=i+next)
	{
		fset=fset+text.charAt(i);
	}
	return fset;
}


public static void rotate(String r,int s)	//method to apply caesar cipher to particular sets depending on the shift s
{
	int i=0;
	int j=0;
	int ch=0;
	char x;
	String caesar="";
	for(i=0;i<r.length();i++)
	{
		x=r.charAt(i);
		j=((x-s-97)%26);
		if(j<0)
		{
			j+=26;
		}
		j+=97;
		caesar=caesar+(char)j;				//shifted string with shift value of s
	}

	j=0;

	for(i=0;i<caesar.length();i++)
	{
		x=caesar.charAt(i);
		ch=(x-'a')%26;
		j=checkfreq(x,caesar);		//finding the frequency of every letter in the shifted string caesar and storing the frequency again in our frequency array alphabet_numeric[] 
		alphabet_numeric[ch]=j;
	}
	
}

public static void decrypt(String d,int len)
{
	int i=0;
	int j=0;
	char x;
	char y;
	int ch1=0;
	int ch2=0;
	int ch=0;
	String plaintext="";
	System.out.println("Decrypted Text is : ");
	for(i=0;i<cipher.length();i++)
	{
		// Print key - encrypted text;
		x=cipher.charAt(i);
		y=d.charAt(j);
		ch1=(int)x-97;		//characters of the original cipher text
		ch2=(int)y-97;		//characters of the key
		ch=(26+(ch1-ch2)%26)%26;	//adding the two and using modulo 26 operation on them
		
		plaintext=plaintext+alphabets[ch];	//getting that character from our global array alphabets and concatenating to the plain text
		j++;
		if(j>=len)	//As key is small, once j reaches length of key, again making it 0.
			j=0;
	}
	System.out.println(plaintext);
}


}
