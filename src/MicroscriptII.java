import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MicroscriptII {
	static boolean isPrime(long n) {
	    if(n < 2) return false;
	    if(n == 2 || n == 3)return true;
	    if(n%2 == 0 || n%3 == 0)return false;
	    long sqrtN=(long)Math.sqrt(n)+1;
	    for(long i=6; i <= sqrtN; i += 6) {
	        if(n%(i-1) == 0 || n%(i+1) == 0) return false;
	    }
	    return true;
	}
	static class Queue{
		class QueueNode{
			QueueNode before,after;
			Object c;
			public String toString(){
				return String.valueOf(c);
			}
		}
		QueueNode first,last;
		void append(Object c){
			QueueNode d=new QueueNode();
			d.c=c;
			if(last!=null)last.after=d;
			d.before=last;
			last=d;
			if(first==null)first=d;
		}
		void duplicate(){
			if(first==null)return;
			QueueNode d=new QueueNode();
			d.c=first.c;
			first.before=d;
			d.after=first;
			first=d;
		}
		boolean isEmpty(){
			return first==null;
		}
		void rotate(){
			if(first==last)return;
			QueueNode d=first;
			first=first.after;
			first.before=null;
			last.after=d;
			d.before=last;
			last=d;
			d.after=null;
		}
		void unrotate(){
			if(first==last)return;
			QueueNode d=last;
			last=last.before;
			last.after=null;
			d.after=first;
			d.before=null;
			first.before=d;
			first=d;
		}
		Object poll(){
			QueueNode d=first;
			if(first.after!=null)first.after.before=null;
			first=first.after;
			return d.c;
		}
		void reverse(){
			Stack<Object>c=new Stack<>();
			while(first!=null){
				c.add(poll());
			}
			while(!c.isEmpty())append(c.pop());
		}
		public boolean equals(Object o){
			if(!(o instanceof Queue))return false;
			
			QueueNode d=first,e=((Queue)o).first;
			if(d==null)return e==null;
			if(e==null)return false;
			while(true){
				if(!Objects.equals(d.c,e.c))return false;
				if(d.after==null&&e.after==null)break;
				if(d.after==null||e.after==null)return false;
			}
			return true;
		}
		Object[]toArray(){
			ArrayList<Object>l=new ArrayList<>();
			QueueNode d=first;
			while(d!=null){
				l.add(d.c);
				d=d.after;
			}
			return l.toArray(new Object[0]);
		}
		public String toString(){
			StringBuilder s=new StringBuilder();
			QueueNode d=first;
			while(d!=null){
				if(s.length()>0){
					s.append(',');
				}
				Object t=d.c;
				if(t instanceof String)t='"'+(String)t+'"';
				s.append(t);
				d=d.after;
			}
			return "["+s+"]";
		}
	}
	static List<Class<?>>types=Arrays.asList(Long.class,Double.class,Boolean.class,String.class,Code.class,Queue.class,Continuation.class);
	static ArrayList<Stack<Object>>stack;
	static Stack<Continuation>cs=new Stack<>();
	static Object x=null,y=null;
	static long t0;
	static Scanner in;
	static int stackId=0;
	static class Code{
		final String c;
		public Code(String co){
			c=co;
		}
		public String toString(){
			return "{"+c+"}";
		}
		public boolean equals(Object o){
			if(o instanceof Code)return ((Code)o).c.equals(c);
			return false;
		}
	}
	static class Continuation{
		final long t;
		final int sid;
		@SuppressWarnings("rawtypes")
		final Stack a,b,c;
		final Object sx,sy;
		@SuppressWarnings({ "rawtypes", "unchecked" })
		void load(){
			x=sx;
			y=sy;
			stackId=sid;
			Stack d=new Stack();
			d.addAll(a);
			stack.set(0,d);
			d=new Stack();
			d.addAll(b);
			stack.set(1,d);
			d=new Stack();
			d.addAll(c);
			stack.set(2,d);
		}
		public boolean equals(Object o){
			return this==o;
		}
		@SuppressWarnings({ "rawtypes", "unchecked" })
		public Continuation(){
			t=(System.nanoTime()-t0)/1000;
			sx=x;
			sy=y;
			a=new Stack();
			a.addAll(stack.get(0));
			b=new Stack();
			b.addAll(stack.get(1));
			c=new Stack();
			c.addAll(stack.get(2));
			sid=stackId;
			cs.push(this);
		}
		public String toString(){
			return "<Continuation @t="+t+"us>";
		}
	}
	public static void main(String[] args){
		stack=new ArrayList<Stack<Object>>()
				{static final long serialVersionUID = 1;{for(int i=0;i<3;i++)add(new Stack<Object>());}};
		in=new Scanner(System.in);
		String code=args.length>0?args[0]:in.nextLine();
		t0=System.nanoTime();
		run(code);
		System.out.println(x);in.close();
	}
	static boolean bool(Object o){
		if(x==null)return false;
		if(x instanceof Boolean){
			return (boolean)x;
		}
		if(x instanceof String){
			return !((String)x).isEmpty();
		}
		if(x instanceof Long){
			return 0!=(long)x;
		}
		if(x instanceof Double){
			return 0!=(double)x;
		}
		if(x instanceof Queue){
			return !((Queue)x).isEmpty();
		}
		return true;
		
	}
	@SuppressWarnings("unchecked")
	static void run(String code){
		for(int i=0;i<code.length();i++){
			if(code.charAt(i)=='\''){
				x=(long)code.charAt(i+1);
				i++;continue ;
			}
			if("0123456789".indexOf(""+code.charAt(i))!=-1){
				int j=i;
				for(;i<code.length()&&"0123456789".indexOf(""+code.charAt(i))!=-1;i++){}
				if(i<code.length()&&code.charAt(i)=='.'){
					for(i++;i<code.length()&&"0123456789".indexOf(""+code.charAt(i))!=-1;i++){}
					x=Double.parseDouble(i>j?code.substring(j,i):"0");
				}else{
					x=Long.parseLong(i>j?code.substring(j,i):"0");
				}
				i--;
				continue;
			}
			if(code.charAt(i)=='-'){
				int j=i;
				if(i<code.length()-1&&Character.isDigit(code.charAt(i+1))){
				for(i++;i<code.length()&&"0123456789".indexOf(""+code.charAt(i))!=-1;i++){}
				if(code.charAt(i)=='.'){
					for(i++;i<code.length()&&"0123456789".indexOf(""+code.charAt(i))!=-1;i++){}
					x=-Double.parseDouble(i>j?code.substring(j,i):"0");
				}else{
					x=-Long.parseLong(i>j?code.substring(j,i):"0");
				}
				i--;}else{
					Object o=stack.get(stackId).pop();
					if(x instanceof Long&&o instanceof Long){
						x=(long)x-(long)o;
					}else if(x instanceof Long&&o instanceof Double){
						x=(long)x-(double)o;
					}else if(x instanceof Double&&o instanceof Long){
						x=(double)x-(long)o;
					}else if(x instanceof Double&&o instanceof Double){
						x=(double)x-(double)o;
					}else if(x instanceof String&&o instanceof String){
						x=((String)x).replaceAll(Pattern.quote((String)o),"");
					}else if(x instanceof Boolean && o instanceof Boolean){
						x=(boolean)x^(boolean)o;
					}else{
						throw new IllegalArgumentException();
					}
				}
				continue;
			}
			if(code.charAt(i)=='"'){
				StringBuilder k=new StringBuilder();
				boolean b=false;
				while(true){
					i++;
					char c=code.charAt(i);
					if(c=='"'){
						if(b){
							k.append('"');
							b=false;
						}else{
							break;
						}
					}else if(c=='\\'){
						if(b){
							k.append('\\');
							b=false;
						}else{
							b=true;
						}
					}else if(c=='n'&&b){
						k.append('\n');
						b=false;
					}else{
						k.append(c);
					}
				}
				x=k.toString();
				continue;
			}
			if(code.charAt(i)=='('&&!bool(x)){
				int j=1;
				for(;j!=0;){
					i++;
					if(i==code.length())return;
					if(code.charAt(i)=='(')j++;
					if(code.charAt(i)==')')j--;
					if(code.charAt(i)=='"'&&code.charAt(i-1)!='\''){
						boolean b=false;
						while(true){
							i++;
							char c=code.charAt(i);
							if(c=='"'){
								if(b){
									b=false;
								}else{
									break;
								}
							}else if(c=='\\'){
								if(b){
									b=false;
								}else{
									b=true;
								}
							}else if(c=='n'&&b){
								b=false;
							}
						}
					}
				}
			}
			if(code.charAt(i)=='['&&bool(x)){
				int k=i;
				int j=1;
				t:for(;j!=0;){
					i++;
					if(i==code.length()){
						break t;
					}
					if(code.charAt(i)=='[')j++;
					if(code.charAt(i)==']')j--;
					if(code.charAt(i)=='"'&&code.charAt(i-1)!='\''){
						if(i==code.length()){
							break t;
						}
						boolean b=false;
						while(true){
							i++;
							char c=code.charAt(i);
							if(c=='"'){
								if(b){
									b=false;
								}else{
									break;
								}
							}else if(c=='\\'){
								if(b){
									b=false;
								}else{
									b=true;
								}
							}else if(c=='n'&&b){
								b=false;
							}
						}
					}
				}
				String s=code.substring(k+1,i);
				while(bool(x))run(s);
			}
			if(code.charAt(i)=='{'){
				int k=i;
				int j=1;
				for(;j!=0;){
					i++;
					if(code.charAt(i)=='{')j++;
					if(code.charAt(i)=='}')j--;
					if(code.charAt(i)=='"'){
						boolean b=false;
						while(true){
							i++;
							char c=code.charAt(i);
							if(c=='"'){
								if(b){
									b=false;
								}else{
									break;
								}
							}else if(c=='\\'){
								if(b){
									b=false;
								}else{
									b=true;
								}
							}else if(c=='n'&&b){
								b=false;
							}
						}
					}
				}
				x=new Code(code.substring(k+1,i));
				continue;
			}
			if(code.charAt(i)=='I'){
				x=in.nextLine();
			}
			if(code.charAt(i)=='='){
				x=Objects.equals(x,stack.get(stackId).pop());
			}
			if(code.charAt(i)=='N'){
				x=Long.parseLong(in.nextLine());
			}
			if(code.charAt(i)=='F'){
				x=Double.parseDouble(in.nextLine());
			}
			if(code.charAt(i)=='`'){
				Object o=x;
				x=y;
				y=o;
			}
			if(code.charAt(i)=='f'){
				if(x instanceof String){
					String s=" "+x+" ";
					int k=0;
					Matcher m=Pattern.compile("%s").matcher(s);
					while(m.find())k++;
					Object[]a=new Object[k];
					if(y instanceof Queue){
						Queue q=(Queue)y;
						for(int j=0;j<k;j++){
							a[j]=q.poll();
						}
					}else{
						for(int j=0;j<k;j++){
							a[j]=stack.get(stackId).pop();
						}
					}
					String[]t=s.split("%s");
					int j;
					StringBuilder st=new StringBuilder();
					for(j=0;j<k;j++){
						st.append(t[j]+a[j]);
					}
					st.append(t[j]);
					s=st.toString();
					x=s.length()>2?s.substring(1,st.length()-1):"";
				}else{
					throw new IllegalArgumentException();
				}
			}
			if(code.charAt(i)=='~'){
				if(x instanceof Long){
					x=~(long)x;
				}else if(x instanceof Code){
					run(((Code)x).c);
				}else if(x instanceof Queue){
					stack.get(stackId).push(((Queue)x).poll());
				}else{
					throw new IllegalArgumentException();
				}					
			}
			if(code.charAt(i)=='e'){
				if(x instanceof Long){
					x=Math.pow(2,(long)x);
				}else if(x instanceof Double){
					x=Math.pow(2,(double)x);
				}else{
					throw new IllegalArgumentException();
				}					
			}
			if(code.charAt(i)=='E'){
				if(x instanceof Long){
					x=Math.pow(10,(long)x);
				}else if(x instanceof Double){
					x=Math.pow(10,(double)x);
				}else{
					throw new IllegalArgumentException();
				}					
			}
			if(code.charAt(i)=='_'){
				if(x instanceof String){
					x=Long.parseLong((String)x);
				}else if(x instanceof Double){
					x=(long)(double)x;
				}else if(x instanceof Boolean){
					x=(boolean)x?1L:0L;
				}else{
					throw new IllegalArgumentException();
				}			
			}
			if(code.charAt(i)=='!'){
				x=!bool(x);				
			}
			if(code.charAt(i)=='?'){
				x=bool(x);				
			}
			if(code.charAt(i)=='&'){
				x=!bool(x)?x:stack.get(stackId).pop();
			}
			if(code.charAt(i)=='|'){
				x=bool(x)?x:stack.get(stackId).pop();
			}
			if(code.charAt(i)=='p'){
				System.out.print(x);
			}
			if(code.charAt(i)=='v'){
				y=x;
			}
			if(code.charAt(i)=='l'){
				x=y;
			}
			if(code.charAt(i)=='P'){
				System.out.println(x);
			}
			if(code.charAt(i)=='n'){
				System.out.println();
			}
			if(code.charAt(i)=='q'){
				System.out.print("\""+x+"\"");
			}
			if(code.charAt(i)=='Q'){
				System.out.println("\""+x+"\"");
			}
			if(code.charAt(i)=='a'){
				Stack<Object>s=stack.get(stackId);
				while(!s.isEmpty())System.out.println(s.pop());
			}
			if(code.charAt(i)=='>'){
				stackId=(1+stackId)%3;
			}
			if(code.charAt(i)=='<'){
				stackId=(2+stackId)%3;
			}
			if(code.charAt(i)=='h'){
				System.exit(0);
			}
			if(code.charAt(i)=='s'){
				stack.get(stackId).add(x);
			}
			if(code.charAt(i)=='o'){
				x=stack.get(stackId).pop();
			}
			if(code.charAt(i)=='C'){
				x=new Continuation();
			}
			if(code.charAt(i)=='L'){
				if(x instanceof Continuation){
					((Continuation)x).load();
				}else{
					cs.pop().load();
				}
			}
			if(code.charAt(i)=='T'){
				x=(System.nanoTime()-t0)/1000;
			}
			if(code.charAt(i)=='D'){
				x=System.currentTimeMillis();
			}
			if(code.charAt(i)=='x'){
				return;
			}
			if(code.charAt(i)=='#'){
				x=(long)stack.get(stackId).size();
			}
			if(code.charAt(i)=='$'){
				x=new Queue();
			}
			if(code.charAt(i)=='k'){
				x=stack.get(stackId).peek();
			}
			if(code.charAt(i)=='@'){
				if(x instanceof Double){
					x=Math.sqrt((double)x);
				}else if(x instanceof Long){
					x=Math.sqrt((long)x);
				}else{
					throw new IllegalArgumentException();
				}
			}
			if(code.charAt(i)=='d'){
				Stack<Object>c=stack.get(stackId);
				c.push(c.peek());
			}
			if(code.charAt(i)=='+'){
				Object o=stack.get(stackId).pop();
				if(x==null){
					x=o;
				}else if(x instanceof Long && o instanceof Long){
					x=(long)x+(long)o;
				}else if(x instanceof Boolean && o instanceof Boolean){
					x=(boolean)x||(boolean)o;
				}else if(x instanceof Long && o instanceof Double){
					x=(long)x+(double)o;
				}else if(x instanceof Double && o instanceof Long){
					x=(double)x+(long)o;
				}else if(x instanceof Long && o instanceof Boolean){
					x=(long)x+((boolean)o?1:0);
				}else if(x instanceof Boolean && o instanceof Long){
					x=((boolean)x?1:0)+(long)o;
				}else if(x instanceof Double && o instanceof Double){
					x=(double)x+(double)o;
				}else if(x instanceof Queue){
					((Queue)x).append(o);
				}else if(x instanceof String){
					x=(String)x+o;
				}else if(x instanceof Code && o instanceof Code){
					x=new Code(((Code)x).c+((Code)o).c);
				}else if(x instanceof Code){
					x=new Code(((Code)x).c+o);
				}else if(o instanceof String){
					x=x+(String)o;
				}else if(o!=null){
					throw new IllegalArgumentException(x.getClass()+" "+o.getClass());
				}
			}
			if(code.charAt(i)=='*'){
				Object o=stack.get(stackId).pop();
				if(x instanceof Long && o instanceof Long){
					x=(long)x*(long)o;
				}else if(x instanceof Boolean && o instanceof Boolean){
					x=(boolean)x&&(boolean)o;
				}else if(x instanceof Long && o instanceof Double){
					x=(long)x*(double)o;
				}else if(x instanceof Double && o instanceof Long){
					x=(double)x*(long)o;
				}else if(x instanceof Double && o instanceof Double){
					x=(double)x*(double)o;
				}else if(x instanceof String && o instanceof Long){
					StringBuilder k=new StringBuilder();
					for(long j=(long)o;j>0;j--){
						k.append(x);
					}
					x=k.toString();
				}else if(x instanceof Long && o instanceof String){
					StringBuilder k=new StringBuilder();
					for(long j=(long)x;j>0;j--){
						k.append(o);
					}
					x=k.toString();
				}else if(x instanceof Long && o instanceof Code){
					for(long j=(long)x;j>0;j--){
						run(((Code)o).c);
					}
				}else if(x instanceof Code && o instanceof Long){
					Code k=(Code)x;
					for(long j=(long)o;j>0;j--){
						run(k.c);
					}
				}else if(x instanceof Long && o instanceof Queue){
					ArrayList<Object>os=new ArrayList<>();
					Queue.QueueNode d=((Queue)o).first;
					Queue q=new Queue();
					while(d!=null){
						os.add(d.c);
						d=d.after;
					}
					for(long j=(long)x;j>0;j--)for(Object p:os)q.append(p);
					x=q;
				}else if(x instanceof Queue && o instanceof Long){
					ArrayList<Object>os=new ArrayList<>();
					Queue.QueueNode d=((Queue)x).first;
					Queue q=new Queue();
					while(d!=null){
						os.add(d.c);
						d=d.after;
					}
					for(long j=(long)o;j>0;j--)for(Object p:os)q.append(p);
					x=q;
				}else{
					throw new IllegalArgumentException();
				}
			}
			if(code.charAt(i)=='%'){
				Object o=stack.get(stackId).pop();
				if(x instanceof Long&&o instanceof Long){
					x=(long)x%(long)o;
				}else if(x instanceof Long&&o instanceof Double){
					x=(long)x%(double)o;
				}else if(x instanceof Double&&o instanceof Long){
					x=(double)x%(long)o;
				}else if(x instanceof Double&&o instanceof Double){
					x=(double)x%(double)o;
				}else{
					throw new IllegalArgumentException();
				}
			}
			if(code.charAt(i)=='/'){
				Object o=stack.get(stackId).pop();
				if(x instanceof Long&&o instanceof Long){
					x=(long)x/(long)o;
				}else if(x instanceof Long&&o instanceof Double){
					x=(long)x/(double)o;
				}else if(x instanceof Double&&o instanceof Long){
					x=(double)x/(long)o;
				}else if(x instanceof Double&&o instanceof Double){
					x=(double)x/(double)o;
				}else{
					throw new IllegalArgumentException();
				}
			}
			if(code.charAt(i)=='t'){
				x=x==null?-1:types.indexOf(x.getClass());
			}
			if(code.charAt(i)==';'){
				if(x instanceof Long && (long)x>0){
					x=isPrime((long)x);
				}else{
					throw new IllegalArgumentException();
				}
			}
			if(code.charAt(i)=='R'){
				if(x instanceof Long){
					x=(long)(Math.random()*(long)x);
				}else if(x instanceof Double){
					x=Math.random()*(double)x;
				}else{
					x=Math.random();
				}
			}
			if(code.charAt(i)=='K'){
				if(x instanceof String){
					String u=(String)x;
					@SuppressWarnings("rawtypes")
					Stack v=new Stack();
					for(char a:u.toCharArray())v.push((long)a);
					while(!v.isEmpty())stack.get(stackId).push(v.pop());
				}else if(x instanceof Long){
					x=String.valueOf((char)(long)x);
				}else{
					throw new IllegalArgumentException(x.getClass().getName());
				}
			}
		}
	}
}
