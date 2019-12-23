import java.io.*;
import java.util.*;

public class vigenere{

static char[] alphabets=new char[]{'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
public static void main(String args[]) throws IOException
{
	BufferedReader brf=new BufferedReader(new InputStreamReader(System.in));
	System.out.println("Enter the cipher text");
	String cipher=brf.readLine();
	decrypt(cipher);
}

public static void decrypt(String c)
{
	char mostfreq;
	String set1=getset(0,4,c);
	//System.out.println("Set 1: " + set1);
	String set2=getset(1,4,c);
	String set3=getset(2,4,c);
	String set4=getset(3,4,c);
	int i=0;
	int j=0;
	String shift="";
	String key="";
	char pass;

  for(j=0;j<4;j++)
  {
	for(i=1;i<=26;i++)
	{
		if(j==0)
		{
			shift=rotate(set1,i);
			//System.out.println("Shifted : "+shift);
			mostfreq=checkfreq(shift);
			//System.out.println("MostFreq : "+mostfreq);
			if(mostfreq=='e')
			{
				//System.out.println("Correct Shift : "+i);
				int k= (int) ('e'-i) % 26;
				pass=(char)('e'-i);
				key=key+pass;
				//System.out.println("First char of key : "+key);
				break;
			}
		}

		if(j==1)
		{
			shift=rotate(set2,i);
			//System.out.println("Shifted : "+shift);
			mostfreq=checkfreq(shift);
			//System.out.println("MostFreq : "+mostfreq);
			if(mostfreq=='t')
			{
				//System.out.println("Correct Shift : "+i);
				int k= (int) ('t'-i) % 26;
				pass=(char)('e'-i);
				key=key+pass;
				//System.out.println("Second char of key : "+key);
				break;
			}
		}

		if(j==2)
		{
			shift=rotate(set3,i);
			mostfreq=checkfreq(shift);
			if(mostfreq=='e')
			{
				pass=(char)('e'-i);
				key=key+pass;
				break;
			}
		}

		if(j==3)
		{
			shift=rotate(set4,i);
			mostfreq=checkfreq(shift);
			if(mostfreq=='e')
			{
				pass=(char)('e'-i);
				key=key+pass;
				break;
			}
		}
	}
  }

	System.out.println("Password is : " + key);
}

public static String rotate(String r,int s)
{
	int i=0;
	int j=0;
	char x;
	String caesar="";
	for(i=0;i<r.length();i++)
	{
		x=r.charAt(i);
		j=((x+s-97)%26+97);
		caesar=caesar+(char)j;
	}
	return caesar;
}

public static char checkfreq(String check)
{
	int i=0;
	int ch=0;
	char x='a';
	int[] count;
	count=new int[26];
	int max=0;
	for(i=0;i<26;i++)
	{
		count[i]=0;
	}
	
	for(i=0;i<check.length();i++)
	{
		ch=check.charAt(i)-'a';
		if(ch<0)
		{
			ch=ch+32;
		}
		ch=ch%26;
		count[ch]++;
	}

	for(i=0;i<26;i++)
	{
		if(count[i]>max)
		{
			x=alphabets[i];
			max=count[i];
		}
	}

	return x;
}

public static String getset(int start,int next,String text)
{
	int i=0;
	String set="";
	for(i=start;i<text.length();i=i+next)
	{
		set=set+text.charAt(i);
	}
	return set;
}


}