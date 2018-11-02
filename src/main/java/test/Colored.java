package test;


import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
//import com.migu.cmam.dbmodel.content.ChargeInfo;
import com.sun.org.apache.xpath.internal.SourceTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by lizhen on 2018/3/14.
 */

class B {
    static String name = "lizhen";

}


class Student implements Cloneable {

    private String name;

    private int age;

    private Professor professor;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    @Override
    public String toString() {
        return "Student [name=" + name + ", age=" + age + ", professor="
            + professor + "]";
    }

    public Student myClone() throws CloneNotSupportedException{
//        return super.clone();
        Student newStudent = (Student) super.clone();
        newStudent.professor = (Professor) professor.myClone();
        return newStudent;
    }

}

class Professor implements Cloneable {

    private String name;

    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Professor [name=" + name + ", age=" + age + "]";
    }

    public Professor myClone() throws CloneNotSupportedException {
        return (Professor)super.clone();
    }
}


enum Fruit {
    APPLE, ORANGE, MELON, peer
}

interface Food {
    default String grow(String seed) {
        return seed + "has grown up!";
    }
}

//@JsonIgnoreProperties({"mount", "place"})
//@JsonFilter("myFilter")
class Dinner implements Cloneable{

    String attr = "father attribution";
//    @JsonIgnore
    String name;

    Integer mount;

    String place;

    LocalDateTime time;

    Integer money;

    static void method(){
        System.out.println("father static method");
    }

    public Dinner() {
    }

    public Dinner(String name, Integer mount, String place, LocalDateTime time, Integer money) {
        this.name = name;
        this.mount = mount;
        this.place = place;
        this.time = time;
        this.money = money;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMount() {
        return mount;
    }

    public void setMount(Integer mount) {
        this.mount = mount;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public Integer getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }

    @Override
    public String toString() {
        return "Dinner{" +
            "name='" + name + '\'' +
            ", mount=" + mount +
            ", place='" + place + '\'' +
            ", time=" + time +
            ", money=" + money +
            '}';
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public String print1(){
        return print2();
    }

    public String print2(){return null;}

    public String print(){
        return "father method";
    }
}

class Lunch extends Dinner{

    String attr = "sonAttr attribution";

    private String extension;

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    @Override
    public String toString() {
        return "Lunch{" +
            "name='" + name + '\'' +
            ", mount=" + mount +
            ", place='" + place + '\'' +
            ", time=" + time +
            ", money=" + money +
            ", extension='" + extension + '\'' +
            '}';
    }

    static void method(){
        System.out.println("son static method");
    }

    public String print2(){return "eaeaeaeaea";}

    public String print(){
        return "son method";
    }
}

class FatherClass {
   public int i=MemberMethod();
   public static int j=staticMethod();
   {
       System.out.println("父类的代码块1");
   }
   {
       System.out.println("父类的代码块2");
   }
   static{
       System.out.println("父类的静态代码块1");
   }
   static{
       System.out.println("父类的静态代码块2");
   }
   public FatherClass() {
       // TODO Auto-generated constructor stub
       System.out.println("父类的构造函数");
   }
   private static int staticMethod() {
       // TODO Auto-generated method stub
       System.out.println("父类的静态方法");
       return 1;
   }

   private int MemberMethod() {
       // TODO Auto-generated method stub
       System.out.println("父类的成员变量");
       return 2;
   }
}

 class SonClass extends FatherClass {

     public int i = MemberMethod();
     public static int j = staticMethod();

     {
         System.out.println("子类的代码块1");
     }

     {
         System.out.println("子类的代码块2");
     }

     static {
         System.out.println("子类的静态代码块1");
     }

     static {
         System.out.println("子类的静态代码块2");
     }

     public SonClass() {
         // TODO Auto-generated constructor stub
         System.out.println("子类的构造函数");
     }

     private static int staticMethod() {
         // TODO Auto-generated method stub
         System.out.println("子类的静态成员变量");
         return 3;
     }

     private int MemberMethod() {
         // TODO Auto-generated method stub
         System.out.println("子类的成员变量");
         return 4;
     }

     public int getI() {
         return i;
     }

     public int getJ() {
         return j;
     }
 }

class Message{
    private Dinner dinner;

    public Dinner getDinner() {
        return dinner;
    }

    public void setDinner(Dinner dinner) {
        this.dinner = dinner;
    }
}

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@interface MyMethodTag {
    String value();

    int number = 1;//不能再使用注解时赋值，可以在注解处理器中使用这个number信息

    boolean judge() default false;

}

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@interface MyFieldTag {
    MyMethodTag value();

}

interface A {
    interface B {

    }

}

public class Colored<K, V> extends AbstractMap<K, V> {

    private static final Logger log = LoggerFactory.getLogger(Colored.class);

    private String name = "初始名字";

    private List<K> keyList = new ArrayList<>();
    private List<V> valueList = new ArrayList<>();
    private Set<Entry<K, V>> set = new HashSet<Entry<K, V>>() {
        @Override
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            for (Iterator<Entry<K, V>> iterator = iterator(); iterator.hasNext(); ) {
                stringBuilder.append(iterator.next().toString() + ",");
            }
            return stringBuilder.toString();
        }

    };

    private class MapEntry<K, V> implements Entry<K, V> {

        private K key;
        private V value;

        MapEntry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V newValue) {
            V oldValue = value;
            value = newValue;
            return oldValue;
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof MapEntry && key.equals(((MapEntry) obj).getKey());
        }

        @Override
        public String toString() {
            return key + "=" + value;
        }

        @Override
        public int hashCode() {
            return key.hashCode();
        }

    }

    public V put(K key, V value) {
        V oldValue = get(key);
        if (!keyList.contains(key)) {
            keyList.add(key);
            valueList.add(value);
            set.add(new MapEntry<K, V>(key, value));
        } else {
            valueList.set(keyList.indexOf(key), value);
            set.remove(new MapEntry<K, V>(key, value));
            set.add(new MapEntry<K, V>(key, value));
        }
        return oldValue;
    }


    @Override
    public V get(Object key) {
        if (!keyList.contains(key)) {
            return null;
        }
        return valueList.get(keyList.indexOf(key));
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        Set<Entry<K, V>> es;
        return (es = set) == null ? set : es;
    }

    static final int tableSizeFor(int cap) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= 1 << 30) ? 1 << 30 : n + 1;
    }

    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    static String URLencode(String str) {
        return str.replaceAll("%", "%25")
            .replaceAll("\\+", "%2B").replaceAll(" ", "%20")
            .replaceAll("#", "%23")
            .replaceAll("&", "%25").replaceAll("=", "%3D");
    }

    static void getSysin(String message) {
        System.out.println(message);
        try {
            new BufferedReader(new InputStreamReader(System.in)).readLine();
        } catch (IOException e) {
//                throw new RuntimeException(e);
            System.out.println(e.getMessage());
        }
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    public static byte[] hexString2Bytes(String hex) {

        if ((hex == null) || (hex.equals(""))){
            return null;
        }
        else if (hex.length()%2 != 0){
            return null; //每个字符表示的都是半个字节，即4位二进制。而一个字符用2个字节表示，所以换成16进制的二进制表示，字符数肯定是2的倍数。
        }
        else{
            hex = hex.toUpperCase();
            int len = hex.length()/2;
            byte[] b = new byte[len]; //每个字符表示的都是半个字节，即4位二进制。所以总字节数是一半。
            char[] hc = hex.toCharArray();
            for (int i=0; i<len; i++){
                int p=2*i;
                b[i] = (byte) (charToByte(hc[p]) << 4 | charToByte(hc[p+1])); // 将hex中的16进制字符2个一对拿出来还原成字节，并放入字节数组中
            }                                                                 // 比如将3和c拿出来运算，得到第一个字节是60
                                                                              //  0000 0011 左移4位， 0011 0000
                                                                              //  0000 1100 或运算， 0011 1100 =32+16+8+4=60
            return b;
        }

    }

    private static String strToBinstr(String str) {
        char[] strChar=str.toCharArray();
        String result="";
        for(int i=0;i<strChar.length;i++){
            result +=Integer.toBinaryString(strChar[i])+ " ";
        }
        return result;
    }

    public static byte[] myTranslate(String hex) {
        char[] chars = hex.toCharArray();
        return null;

    }

    private static class InnerClass{
        private Colored colored;

        public InnerClass(Colored colored){
            this.colored = colored;
        }

        public String getOutName(){
            return colored.name;
        }



    }

    public InnerClass innerClass(){
        return new InnerClass(this);
    }

    public static String toUpper(final Fanxing<String> fanxing){
        Fanxing<String> fanxing1 = fanxing;
        fanxing1 = null;
        System.out.println(fanxing1);
        return "changed";
    }

    @MyMethodTag("first")
    public static void main(String[] args) throws InterruptedException {
        //        Fruit f1 = new Fruit(1);
//        Fruit f2 = new Fruit(2);
//
//        Map<Fruit,String> map = new HashMap<Fruit, String>();
//        System.out.println(f1 == f2);
//        System.out.println(f1.equals(f2));
//
//        map.put(f1,"f1");
//        map.put(f2,"f2");
//
//        System.out.println(map.get(f1));
//        System.out.println(map.get(f2));
//
//        Fruit f3 = new Fruit(1);
//        System.out.println(f1 == f3);
//
//        String s1 = new String("a");
//        String s2 = new String("a");
//        System.out.println(s1 == s2);


//        List<String> list = Arrays.asList("a b c d e f g h".split(" "));
//        List<String> list2 = new ArrayList<>(list);
//        System.out.println(list2.get(1));
//        list2.set(1,"changed");
//        System.out.println(list2.get(1));
//        System.out.println(list.get(1));
//
//        System.out.println("--------------");
//
//        String[] strings = "a b c d e f g h".split(" ");
//        String[] target = new String[8];
//        System.arraycopy(strings,0,target,0,8);
//        System.out.println(Arrays.toString(target));
//        strings[1] = "1";
//        System.out.println(Arrays.toString(target));
//
//        System.out.println("--------------");
//        Fruit f1 = new Fruit(1);
//        Fruit f2 =  new Fruit(2);
//        Fruit f3 = new Fruit(3);
//        Fruit[] fruits = new Fruit[]{f1,f2,f3};
//        Fruit[] fruits1 = new Fruit[3];
//        System.arraycopy(fruits,0,fruits1,0,3);
//        System.out.println(Arrays.toString(fruits1));
//        f2.i=4;
//        System.out.println(Arrays.toString(fruits1));
//
//        System.out.println("--------------");
//
//        SortedSet<String> sortedSet = new TreeSet<>();
//        Collections.addAll(sortedSet,"one two three four".split(" "));
//
//        System.out.println(sortedSet.comparator() == null?"null":"not null");
//
//        System.out.println("--------------");
//
//        class Student implements Comparable<Student>{
//            int i;
//            int j;
//            Student(int i , int j){
//                this.i = i;
//                this.j = j;
//            }
//
//            @Override
//            public String toString() {
//                return super.toString() + " And i: "+ i + " And j: "+ j;
//            }
//
//            @Override
//            public int compareTo(Student o) {
//                if(i>o.i){
//                    return 1;
//                }
//                if(i==o.i){
//                    if(j>o.j){
//                        return 1;
//                    }else if(j==o.j){
//                        return 0;
//                    }
//                }
//                return -1;
//            }
//        }
//        Student[] students =  new Student[]{new Student(2,2),new Student(3,1),new Student(1,3)};
//        Arrays.sort(students);
//
//        System.out.println(Arrays.toString(students));
//
//        Queue<Student> priorityQueue = new PriorityQueue();
//        priorityQueue.add(new Student(2,2));
//        priorityQueue.add(new Student(3,1));
//        priorityQueue.add(new Student(1,3));
//
//        while (!priorityQueue.isEmpty()){
//            System.out.println(priorityQueue.remove());
//        }
//
//        System.out.println("--------------");
//
//        log.info("imagePath: {}","12345");
//
//        System.out.println("--------------");
//
//       Arrays.stream("2".split(",")).forEach(category -> category.matches("1|2|3|4|5|6|7"));
//       System.out.println(Arrays.stream("2,3,8".split(",")).allMatch(category -> category.matches("1|2|3|4|5|6|7")));
//       System.out.println(Arrays.stream("1|2|21".split("\\|")).allMatch(pro -> pro.matches("1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|17|18|19|20")));

//        Colored<Integer,String> colored = new Colored<>();
//        colored.put(1,"a");
//        colored.put(2,"b");
//        colored.put(3,"c");
//        colored.put(4,"d");
//
//        System.out.println(colored);
//        System.out.println(colored.get(2));
////        System.out.println(Arrays.toString(colored.entrySet().toArray()));
//        System.out.println((colored.entrySet()));
//
//        colored.put(2,"e");
//        System.out.println((colored.entrySet()));


//        for (int i : new int[]{0,1,2,4,8,16,1024}) {
//            System.out.println(i + ":" + tableSizeFor(i));
//        }

//        System.out.println(isNumeric("12Avc"));
//
//        String a = "  a ";
//        System.out.println(a);
//        String b = a.replaceAll("^(\\s)*", "");
//        String c = b.replaceAll("(\\s)*$", "");
//        System.out.println(a);
//        System.out.println(b);
//        System.out.println(c);

//        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
//        try {
//            Date date = sf.parse("20180313000000");
//            System.out.println(date);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        String str = "[{\"formType\": \"8\",\"prdContId\": \"608583204\",\"AssetMGList\": [{\"assetId\": \"1000\",\"name\": \"lizhen\"}, {\"assetId\": \"1001\",\"name\": \"zhujianghong\"}]}, {\"formType\": \"8\",\"prdContId\": \"608583204\",}]";
//        JSONArray jsonArray = JSONArray.parseArray(str);
//        JSONObject contentMG = (JSONObject) jsonArray.get(0);
//        JSONObject assetMG = ((JSONArray) contentMG.get("AssetMGList")).getJSONObject(0);
//        System.out.println(assetMG);

//        BitSet bitSet = new BitSet(65);
//        System.out.println(bitSet.size());
//        bitSet.set(1);
//        bitSet.set(4);
//        bitSet.set(10);
//        System.out.println(bitSet.length());
//        System.out.println(bitSet);
//        System.out.println(Arrays.toString(bitSet.toLongArray()));
//        System.out.println(Arrays.toString(bitSet.toByteArray()));
//        System.out.println(-1 >> 33);
//        long[] longs = new long[0];

//        List<Long> countList = Arrays.asList(1L,2L,3L,4L,5L);
//        List<String> stringList = Arrays.asList("1","2","3","4","5");
//        Long total = countList.parallelStream().reduce(1L, Long::sum);
//        Optional<String> total1 = stringList.parallelStream().reduce((s1, s2) -> s1 + " # " + s2);
//        System.out.println(total);
//        System.out.println(total1);


//        try {
//            File tempFile = File.createTempFile("vehicle", ".csv");
//            String fileName = tempFile.getCanonicalPath();
//            System.out.println(fileName);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        ExecutorService exec = Executors.newSingleThreadExecutor();
//        for (int i = 0 ; i<3;i++){
//            exec.execute(new Runnable() {
//                int count = 1;
//                int d = 0;
//                @Override
//                public void run() {
////                    while (!Thread.interrupted()){
//                        while (count-->0){
//                            for(int i = 1 ; i<1000000000;i++){
////                                while (Thread.currentThread().isInterrupted()){
////                                    System.out.println("#0"+this.toString() + Thread.interrupted());
////                                }
//                                d+=(Math.PI+Math.E)/(double)i; //可阻塞的操作，被中断后提前在方法中返回？——不是吧？
//                                while (Thread.currentThread().isInterrupted()){
//                                    System.out.println("#1"+this.toString() + Thread.interrupted());
//                                }
//                            }
//                            System.out.println("#2"+this.toString() + Thread.interrupted());
//                            System.out.println(d);
//                            System.out.println(this);
////                        try {
////                            TimeUnit.SECONDS.sleep(1l);
////                        } catch (InterruptedException e) {
////                            e.printStackTrace();
////                        }
//                        }
//                    }
////                }
//            });
//        }
//        try {
//            TimeUnit.MILLISECONDS.sleep(100l);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        exec.shutdownNow();// shutdown被调用之前，exec只提交了一个任务，而run()方法中没有可中断的方法调用，所以即使被发出了interrupt()信号，也不妨碍它继续执行，而后面的2个任务不会被提交运行了。
//        System.out.println("come back to main()");

//        class IOBlocked implements Runnable{
//
//            private InputStream in;
//            public IOBlocked(InputStream is){in = is;}
//            @Override
//            public void run() {
//                try {
//                    System.out.println("waiting for read()");
//                    in.read();
//                } catch (IOException e) {
//                    if(Thread.currentThread().isInterrupted()){
//                        System.out.println("interrupted from blocked I/O");
//                    }else {
//                        throw new RuntimeException();
//                    }
//                }
//                System.out.println(Thread.currentThread().isInterrupted());
//                System.out.println("Exiting IOBlocked.run()");
//            }
//        }
//        ExecutorService exec = Executors.newCachedThreadPool();
//        Future<?> f = exec.submit(new IOBlocked(System.in));
//        TimeUnit.MILLISECONDS.sleep(100);
//        System.out.println("interruptting");
//        f.cancel(true);
//        System.out.println("interrupt has been sent");

//        String rawURL= "http://183.192.190.11:8088/video/store/copyright/1000/40373/2018年5月 咪咕免责声明-2#.jpg";
//        try {
//           String encodedURL = URLEncoder.encode(rawURL,"UTF-8");
//           String decodedURL = URLDecoder.decode(encodedURL,"UTF-8");
//           String correctURL = URLencode(rawURL);
//           System.out.println(correctURL);
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }

//        Class classInt=int.class;
//        Class classInteger=Integer.class;
//        System.out.println(classInt.getSimpleName());
//        System.out.println(classInteger.getSimpleName());

//         class ThreadTest implements Runnable {
//             int i = 0;
//             ThreadTest(int i ){this.i = i;}
//            public void run() {
//                System.out.println("I'm running!");
//                if(Thread.currentThread().isInterrupted()){
//                    System.out.println("I'm interrupted!");
//                }
//            }
//        }
//
//        ExecutorService exec = Executors.newSingleThreadExecutor();
//         exec.execute(new ThreadTest(1));
//
//        ThreadTest tt = new ThreadTest(2);
//        Thread t = new Thread(tt);
//        t.start();
//        t.interrupt();
//
//        TimeUnit.SECONDS.sleep(5);
//        exec.execute(new ThreadTest(3));

//        class MyThread extends Thread {
//            int d;
//            @Override
//            public void run() {
////                super.run();
//                for (int i = 0; i < 50000000; i++) {
//                    d+=(Math.PI+Math.E)/(double)i;
//                }
//                System.out.println("MyThread run()中断检查前");
//                System.out.println("是否停止2？=" + Thread.interrupted());//false
//                System.out.println("MyThread run()方法执行完毕");
//            }
//        }
//
//        Object object = new Object();
//        object.wait();
//        MyThread thread = new MyThread();
//        thread.start();
//        Thread.sleep(10);
//        System.out.println("调用interrupt()方法");
//        thread.interrupt();
////        Thread.currentThread().interrupt();
//        System.out.println("是否停止1？=" + Thread.currentThread().isInterrupted());//false
//
//        System.out.println("是否停止3？=" + thread.interrupted());//false

//        byte b = 127;
//        String str = Integer.toBinaryString(b);
//        System.out.println(str);
//
//
//        new Iterable(){
//
//            @Override
//            public Iterator iterator() {
//                return new Iterator() {
//                    @Override
//                    public boolean hasNext() {
//                        return false;
//                    }
//
//                    @Override
//                    public Object next() {
//                        return null;
//                    }
//                };
//            }
//        };

//        int start = 6;
//        int rows = 3;
//        int limit = start+rows<=8?start+rows:8;
//        List<Long> data = Arrays.<Long>asList(10l,9l,7l,1l,2l,100l,20l,10l);
//        data.sort((o1,o2) -> (o1<o2?-1:(o1.equals(o2)?0:1)));
//        List<Long> dataReturn = data.subList(start,limit);
//        System.out.println(Arrays.toString(dataReturn.toArray()));

//        Pattern pattern = Pattern.compile("((abc)+)d");
//        Matcher matcher = pattern.matcher("abcabcabcdabcdabcabcdaaa");
////        System.out.println(matcher.matches());
////        System.out.println(matcher.lookingAt());
//////        System.out.println(matcher.find(10));
////        System.out.println(matcher.group());
////        System.out.println(matcher.groupCount());
////        System.out.println(matcher.group(1));
//
//        StringBuffer sbuf = new StringBuffer();
//        int i=0;
//        while (matcher.find()){
//            matcher.appendReplacement(sbuf,"替换#"+i);
//            i++;
//        }
//        matcher.appendTail(sbuf);
//        System.out.println(sbuf.toString());
//        Class cla = Number.class;
//        Class clb = Integer.class;
//        System.out.println(cla.isInstance(1));
//        System.out.println(cla.isAssignableFrom(clb));


//        System.out.println(Part.partFactories.get(0).create());
//        System.out.println(Part.partFactories.get(1).create());

//        getSysin("press 'enter' (" + "aaaaa" +")");
//        System.out.println("finish");

//        System.out.println("转义HTML,注意汉字:" + StringEscapeUtils.escapeHtml("<font>chen磊  xing</font>"));    //转义HTML,注意汉字
//        System.out.println("反转义HTML:" + StringEscapeUtils.unescapeHtml("&lt;font&gt;chen&#30922;  xing&lt;/font&gt;"));  //反转义HTML
//
//        String str = "{\"total\":1,\"code\":\"0000\",\"data\":[{\"channelMark\":\"\",\"accessName\":\"接入方测试\",\"isStart\":0,\"encryptKey\":\"\",\"accessId\":\"20180516\",\"autoSend\":0,\"id\":\"5ae0a4fbae020b0008c63014\",\"beginTime\":\"2018-04-25T00:00:00\",\"endTime\":\"2018-04-26T00:00:00\",\"isIncrementalOutput\":0},{\"channelMark\":\"\",\"accessName\":\"abc0427\",\"isStart\":0,\"encryptKey\":\"c8d0f407-91da-491b-9110-4cb9822b5db1\",\"autoSend\":0,\"timeInterval\":1,\"id\":\"5ae19b1bae020b0008c63015\",\"beginTime\":\"2018-04-27T00:00:00\",\"endTime\":\"2018-07-08T00:00:00\",\"isIncrementalOutput\":1},{\"channelMark\":\"111\",\"accessName\":\"111\",\"isStart\":0,\"encryptKey\":\"7915d8e7-f4a7-48c3-a0e6-decf44297193\",\"accessId\":\"11111\",\"autoSend\":0,\"id\":\"5af1682e690b5e0008d21a6d\",\"beginTime\":\"2018-05-04T00:00:00\",\"endTime\":\"2018-05-11T00:00:00\",\"isIncrementalOutput\":0},{\"channelMark\":\"10086\",\"accessName\":\"咪咕1\",\"isStart\":0,\"encryptKey\":\"21a3865b-a8c0-40b6-a4ad-05cfcff69fa3\",\"accessId\":\"10086\",\"autoSend\":0,\"id\":\"5af169f8690b5e0008d21a6e\",\"beginTime\":\"2018-05-08T00:00:00\",\"endTime\":\"2018-05-25T00:00:00\",\"isIncrementalOutput\":0},{\"channelMark\":\"10001\",\"accessName\":\"咪咕2\",\"isStart\":0,\"encryptKey\":\"afd54970-7225-4b85-bcca-3bd9bea388b1\",\"accessId\":\"10001\",\"autoSend\":0,\"id\":\"5af16a10690b5e0008d21a6f\",\"beginTime\":\"2018-05-08T00:00:00\",\"endTime\":\"2018-05-31T00:00:00\",\"isIncrementalOutput\":0},{\"channelMark\":\"19999\",\"accessName\":\"咪咕+\",\"isStart\":0,\"encryptKey\":\"5b5bcb4a-b065-4eb6-b8b6-1c3d5793a0c8\",\"accessId\":\"19999\",\"autoSend\":0,\"timeInterval\":0,\"id\":\"5af1876e690b5e0008d21a70\",\"beginTime\":\"2018-05-08T00:00:00\",\"endTime\":\"2018-05-31T00:00:00\",\"isIncrementalOutput\":1},{\"channelMark\":\"1321231\",\"accessName\":\"水电费\",\"isStart\":0,\"encryptKey\":\"238b1475-db47-4b62-b11d-9281c9eaea6e\",\"accessId\":\"水电费\",\"autoSend\":0,\"id\":\"5af92a1d83603000082a56a9\",\"beginTime\":\"2018-01-10T00:00:00\",\"endTime\":\"2018-06-24T00:50:00\",\"isIncrementalOutput\":0},{\"channelMark\":\"\",\"accessName\":\"migu k\",\"isStart\":0,\"encryptKey\":\"f2030375-0471-443f-8709-53eeae8d0a8f\",\"accessId\":\"\",\"autoSend\":0,\"id\":\"5afb8df983603000082a56ac\",\"beginTime\":\"2018-05-16T00:00:00\",\"endTime\":\"2018-05-16T00:00:00\",\"isIncrementalOutput\":0},{\"channelMark\":\"121212\",\"accessName\":\"migu+\",\"isStart\":0,\"encryptKey\":\"\",\"accessId\":\"121212\",\"autoSend\":0,\"id\":\"5afe2cfd83603000089fb010\",\"beginTime\":\"2018-05-18T00:00:00\",\"endTime\":\"2018-05-30T00:00:00\",\"isIncrementalOutput\":0},{\"channelMark\":\"232323\",\"accessName\":\"migu-\",\"isStart\":0,\"encryptKey\":\"\",\"accessId\":\"232323\",\"autoSend\":0,\"id\":\"5afe2f548360300008297019\",\"isIncrementalOutput\":0},{\"channelMark\":\"9999\",\"accessName\":\"<aaa>\",\"isStart\":0,\"encryptKey\":\"\",\"accessId\":\"9999\",\"autoSend\":0,\"id\":\"5b023b07836030000829701b\",\"beginTime\":\"2018-05-21T00:00:00\",\"isIncrementalOutput\":0}],\"message\":\"success\",\"totalCount\":11}";
//        JSONObject jsonObject = JSONObject.parseObject(str);
//        JSONArray jsonArray = (JSONArray) jsonObject.get("data");
//        for (int i = 0; i < jsonArray.size(); i++) {
//            JSONObject jsonObjectSon = jsonArray.getJSONObject(i);
//            Set<Map.Entry<String, Object>> entrySetSon = jsonObjectSon.entrySet();
//            for (Map.Entry<String, Object> entrySon : entrySetSon) {
//                Object valueSon = entrySon.getValue();
//                String keySon = entrySon.getKey();
//                if (valueSon instanceof String) {
//                    valueSon = StringEscapeUtils.escapeHtml((String) valueSon);
//                    jsonObjectSon.replace(keySon, valueSon);
//                }
//            }
//        }
//        jsonObject.replace("data", jsonArray);
//        str = jsonObject.toString();
//        System.out.println(str);


        /**
         * 将从文件读取的内容写入StringWriter，然后从中读出字符串
         */
//        StringWriter stringWriter = new StringWriter();
//        PrintWriter printWriter = new PrintWriter(stringWriter);
//        FileReader reader = null;
//        char c = ' ';
//        try {
//            reader = new FileReader("D:\\cm\\video-aggregation-service\\src\\main\\java\\com\\migu\\cmam\\video\\web\\rest\\LogsResource.java");
//            while((c = (char)reader.read()) != 65535){ //因为将read()方法返回的int转为char，所以原来用-1判断结尾不正确，应该用其绝对值65535
//                printWriter.print(c);
//            }
//            StringBuffer context = stringWriter.getBuffer();
//            System.out.println(context.toString());
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }catch (IOException e1){
//            e1.printStackTrace();
//        }
//        finally {
//            if(reader !=null){
//                try {
//                    reader.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }

//        char x = (char) -1;
//        System.out.println(Integer.valueOf(x));

        /**
         * 将字符串str的UTF-8的16进制表示还原成字符串内容
         */
        //参数是16进制的二进制表示，传入的时候把"0x"去掉
//        byte[] bytes = hexString2Bytes("3c5554462d383e0a50656b696e6720556e697665727369747920746561636865727320616e6420636c6173736d617465733a0a486f772061726520796f75210a4920616d205975656c756f2066726f6d20746865203230313420466f726569676e204c616e67756167657320496e737469747574652e204920776173206f6e65206f66207468652065696768742073747564656e74732077686f207375626d69747465642074686520e2809c496e666f726d6174696f6e20446973636c6f73757265204170706c69636174696f6e20466f726de2809d20746f2050656b696e6720556e6976657273697479206f6e20746865206d6f726e696e67206f6620417072696c20392e20492064726167676564206d7920746972656420626f647920616e642077726f74652074686973207465787420746f20696c6c7573747261746520736f6d65206f6620746865207468696e6773207468617420686176652068617070656e656420746f206d6520726563656e746c792e0a6f6e650a416674657220417072696c203974682c20492077617320636f6e7374616e746c7920696e74657276696577656420627920666163756c7479206d656d6265727320616e64206c656164657273206f662074686520636f6c6c6567652c20616e6420697420636f6e74696e75656420747769636520756e74696c206d69646e69676874206f72206576656e2074776f2e20447572696e672074686520636f6e766572736174696f6e2c207468652073747564656e7420746561636865722072657065617465646c79206d656e74696f6e6564202243616e20796f75206772616475617465207375636365737366756c6c792c222022446f207768617420796f7572206d6f7468657220616e64206772616e646d6f7468657220646f2c2220616e64202253747564656e7420746561636865722068617320746865207269676874206e6f7420746f20636f6e7461637420796f75206469726563746c7920627920796f757220706172656e74732e22205768696c6520492077617320707265706172696e67206d792067726164756174696f6e2074686573697320726563656e746c792c206672657175656e7420696e74657272757074696f6e7320616e642073756273657175656e742070737963686f6c6f676963616c20707265737375726573207365766572656c79206166666563746564206d79207468657369732077726974696e672e0a74776f0a4174206e6f6f6e206f6e20417072696c2032302c20492072656365697665642061207265706c792066726f6d20746865207363686f6f6c2e2054686520536563726574617279206f662074686520506172747920436f6d6d6974746565206f6620746865205363686f6f6c206f6620466f726569676e204c616e6775616765732c207468652074656163686572206f662074686520587565676f6e6720616e64207468652068656164207465616368657220776572652070726573656e742e2054686520736563726574617279206f662074686520506172747920436f6d6d6974746565207265616420746f206d652074686520616e7377657220746f20746865206f70656e20696e666f726d6174696f6e206170706c69636174696f6e206f6620746865207363686f6f6c3a0a312e2044697363757373696e672074686174205368656e79616e672053686964652773206d656574696e67206c6576656c206973206e6f7420656e6f7567680a322e2054686520696e7665737469676174696f6e20726573756c7473206f6620746865205075626c69632053656375726974792042757265617520617265206e6f7420696e207468652073636f7065206f66207363686f6f6c206d616e6167656d656e740a332e2054686520636f6e74656e74206f6620746865207075626c69632072657669657720696e205368656e79616e6720776173206e6f7420666f756e642064756520746f20776f726b206572726f727320696e20746865204368696e657365204465706172746d656e742e0a54686520726573756c74206f662074686973207265706c79206469736170706f696e746564206d652e20486f77657665722c20746865207375626d697373696f6e206f662067726164756174696f6e207468657369732077696c6c20736f6f6e20626520636c6f7365642c20492063616e206f6e6c7920666f637573206f6e207468657369732077726974696e672e0a74687265650a41742061626f757420313120706d206f6e20417072696c2032322c2074686520636f756e73656c6f722073756464656e6c792063616c6c6564206d652c206275742062656361757365207468652074696d6520776173206c6174652c204920646964206e6f7420726563656976652069742e204174206f6e65206f27636c6f636b20696e20746865206d6f726e696e672c2074686520636f756e73656c6f7220616e6420686973206d6f746865722073756464656e6c792063616d6520746f206d7920646f726d69746f727920616e6420666f72636564206d6520746f2077616b6520757020616e642061736b6564206d6520746f2064656c65746520616c6c2074686520696e666f726d6174696f6e2072656c6174656420746f2074686520696e666f726d6174696f6e20646973636c6f7375726520696e636964656e74206f6e206d79206d6f62696c652070686f6e6520616e6420636f6d70757465722e204166746572206461776e2c20492077656e7420746f20746865207465616368657220746f206d616b652061207772697474656e2067756172616e74656520496e766f6c76656420696e2074686973206d61747465722e2053747564656e7473206f6e207468652073616d6520666c6f6f722063616e20746573746966792e204c617465722c2049207761732074616b656e20686f6d65206279206d7920706172656e747320616e6420492063616e27742072657475726e20746f207363686f6f6c20617420746865206d6f6d656e742e0a4d79206d6f7468657220616e6420492077657265206177616b6520616c6c206e696768742e205363686f6f6c73206d6973726570726573656e74656420746865206661637473207768656e207468657920636f6e746163746564207468656972206d6f74686572732c2063617573696e67206d6f746865727320746f206265206f7665726c7920667269676874656e656420616e6420656d6f74696f6e616c6c7920636f6c6c61707365642e2042656361757365206f662074686520756e726561736f6e61626c6520696e74657276656e74696f6e206f6620746865207363686f6f6c2c206d792072656c6174696f6e736869702077697468206d79206d6f746865722077617320616c6d6f73742062726f6b656e2e205468652063757272656e7420616374696f6e206f662074686520636f6c6c6567652068617320616c72656164792062726f6b656e2074686520626f74746f6d206c696e652e2049206665656c206665617220616e6420616e6765722e0a5768617420697320746865206372696d65206f66206170706c79696e6720666f7220696e666f726d6174696f6e20646973636c6f737572653f204920646964206e6f7420646f20616e797468696e672077726f6e672c206e6f7220776f756c6420492072656772657420686176696e67207375626d6974746564207468652022496e666f726d6174696f6e20446973636c6f73757265204170706c69636174696f6e20466f726d2220746f206578657263697365206d7920686f6e6f7261626c6520726967687473206173206120636f6c6c6567652073747564656e7420696e204265696a696e672e0a5477656e7479207965617273206f6620656e76792c2049206c6f7665206d79206d6f746865722e20496e207468652066616365206f662068657220736f72726f7766756c206372792c2073656c662d7361637269666963652c206b6e65656c696e672072657175657374732c20616e642074687265617473206f6620737569636964652c206d7920686561727420697320626c656564696e672e20496e2068657220706c65612c20492063616e206f6e6c792072657475726e20686f6d652074656d706f726172696c792c2062757420492063616e2774207374616e64206261636b2066726f6d207072696e6369706c652e20436f6d70726f6d6973652063616e277420736f6c766520616e792070726f626c656d732e20492068617665206e6f2063686f6963652062757420746f20777269746520746869732073746174656d656e7420616e64207374617465207468652073746f72792e0a456d6f74696f6e616c2c20706c6561736520666f7267697665206d6520666f72206265696e6720696e636f686572656e742e0a666f75720a486572652c204920666f726d616c6c79207375626d69747465642074686520666f6c6c6f77696e672064656d616e647320746f20746865205363686f6f6c206f6620466f726569676e204c616e67756167657320e2808be2808b6f662050656b696e6720556e69766572736974793a0a312e20546865205363686f6f6c206f6620466f726569676e204c616e67756167657320e2808be2808b6f662050656b696e6720556e69766572736974792073686f756c64206f70656e6c7920737461746520696e2077726974696e67207468652072756c657320616e6420726567756c6174696f6e73206f6e207768696368204920686176652070726573737572656420706172656e74732c20666f72636564206d6520746f20696e74657276696577206d6520696e20746865206d6f726e696e6720616e642061736b6564206d6520746f2064656c657465207468652072656c6576616e7420696e666f726d6174696f6e206f6e2074686520646973636c6f73757265206f66206170706c69636174696f6e20696e666f726d6174696f6e2e20496c6c6567616c20616e64206e6f6e2d636f6d706c69616e6365206f7065726174696f6e732073686f756c6420626520636c6561726c7920646566696e656420647572696e6720746869732070726f636573732e20416e642074616b65206d6561737572657320746f2070726576656e74207375636820696e636964656e74732066726f6d2068617070656e696e6720616761696e2e0a322e205468652050656b696e6720556e6976657273697479205363686f6f6c206f6620466f726569676e204c616e67756167657320e2808be2808b73686f756c6420696d6d6564696174656c7920636561736520616c6c2061637473206f66207072657373757265206f6e206d792066616d696c792c20666f726d616c6c792061706f6c6f67697a6520746f206d7920616c726561647920667269676874656e6564206d6f746865722c20636c6172696679207468652066616374732c20616e642068656c7020726570616972207468652066616d696c792074656e73696f6e206361757365642062792074686520696e636964656e742e0a332e20546865205363686f6f6c206f6620466f726569676e204c616e67756167657320e2808be2808b6f662050656b696e6720556e6976657273697479206d757374206d616b6520616e206f70656e207772697474656e2067756172616e74656520746861742074686973206d61747465722077696c6c206e6f7420616666656374206d792067726164756174696f6e20616e642077696c6c206e6f7420636f6e74696e756520746f20696e746572666572652077697468206d7920646973736572746174696f6e2077726974696e672070726f636573732e0a342e20546865205363686f6f6c206f6620466f726569676e204c616e67756167657320e2808be2808b6f662050656b696e6720556e697665727369747920697320726573706f6e7369626c6520666f7220656c696d696e6174696e6720616c6c20616476657273652065666665637473206f662074686973206d6174746572206f6e206d7920737475646965732c2066757475726520656d706c6f796d656e7420616e642066616d696c79206d656d626572732e0a352e20546865205363686f6f6c206f6620466f726569676e204c616e67756167657320e2808be2808b6f662050656b696e6720556e69766572736974792073686f756c64206d616b65206120636c656172207772697474656e20726573706f6e736520746f207468652061707065616c20616e64206769766520697420746f2065766572796f6e6520636f6e6365726e65642e0a492077696c6c2072657461696e20616c6c206c6567616c2072696768747320746f206675727468657220696e7665737469676174652074686520726573706f6e736962696c6974696573206f662072656c6576616e7420696e646976696475616c7320616e6420756e6974732c20696e636c7564696e6720627574206e6f74206c696d6974656420746f207265706f7274696e6720746f2050656b696e6720556e697665727369747920616e642068696768657220617574686f7269746965732074686520736572696f75732076696f6c6174696f6e206f66207363686f6f6c20726567756c6174696f6e732062792074686520466f726569676e204c616e677561676520496e737469747574652e0a50656b696e6720556e6976657273697479205363686f6f6c206f6620466f726569676e204c616e6775616765730a417072696c2032332c20323031380a0ae58c97e4baace5a4a7e5ada6e79a84e88081e5b888e5928ce5908ce5ada6efbc9a0ae4bda0e4bbace5a5bdefbc810ae68891e698af32303134e7baa7e5a496e59bbde8afade5ada6e999a2e79a84e5b2b3e69895efbc8ce698af34e69c8839e697a5e697a9e4b88ae59091e58c97e4baace5a4a7e5ada6e98092e4baa4e3808ae4bfa1e681afe585ace5bc80e794b3e8afb7e8a1a8e3808be79a84e585abe4bd8de588b0e59cbae5908ce5ada6e4b98be4b880e38082e68891e68b96e79d80e69e81e796b2e683abe79a84e8baabe8baafe58699e4b88be8bf99e6aeb5e69687e5ad97efbc8ce8afb4e6988ee8bf91e69da5e58f91e7949fe59ca8e68891e8baabe4b88ae79a84e4b880e4ba9be4ba8be68385e380820ae4b8800a34e69c8839e697a5e4b98be5908eefbc8ce68891e4b88de696ade8a2abe5ada6e999a2e5ada6e5b7a5e88081e5b888e38081e9a286e5afbce7baa6e8b088efbc8ce5b9b6e4b8a4e6aca1e68c81e7bbade588b0e5878ce699a8e4b880e782b9e7949ae887b3e4b8a4e782b9e38082e59ca8e8b088e8af9de4b8adefbc8ce5ada6e5b7a5e88081e5b888e5a49ae6aca1e68f90e588b0e2809ce883bde590a6e9a1bae588a9e6af95e4b89ae2809de38081e2809ce5819ae8bf99e4b8aae4bda0e6af8de4bab2e5928ce5a7a5e5a7a5e6808ee4b988e79c8be2809de38081e2809ce5ada6e5b7a5e88081e5b888e69c89e69d83e4b88de7bb8fe8bf87e4bda0e79bb4e68ea5e88194e7b3bbe4bda0e79a84e5aeb6e995bfe2809de38082e8808ce68891e8bf91e69c9fe6ada3e59ca8e58786e5a487e6af95e4b89ae8aebae69687efbc8ce9a291e7b981e79a84e68993e689b0e5928ce5908ee7bbade79a84e5bf83e79086e58e8be58a9be4b8a5e9878de5bdb1e5938de4ba86e68891e79a84e8aebae69687e58699e4bd9ce380820ae4ba8c0a34e69c883230e697a5e4b8ade58d88efbc8ce68891e694b6e588b0e4ba86e6a0a1e696b9e79a84e59b9ee5a48de38082e5a496e59bbde8afade5ada6e999a2e5859ae5a794e4b9a6e8aeb0e38081e5ada6e5b7a5e88081e5b888e38081e78fade4b8bbe4bbbbe59ca8e59cbaefbc8ce5859ae5a794e4b9a6e8aeb0e59091e68891e5aea3e8afbbe4ba86e5ada6e6a0a1e5afb9e4ba8ee69cace6aca1e4bfa1e681afe585ace5bc80e794b3e8afb7e79a84e7ad94e5a48defbc9a0a31e38081e8aea8e8aebae6b288e998b3e5b888e5beb7e79a84e4bc9ae8aeaee7baa7e588abe4b88de5a49fe8aeb0e5bd950a32e38081e585ace5ae89e5b180e8b083e69fa5e7bb93e69e9ce4b88de59ca8e5ada6e6a0a1e79a84e7aea1e79086e88c83e59bb4e9878c0a33e38081e6b288e998b3e585ace5bc80e6a380e8aea8e79a84e58685e5aeb9e59ba0e4b8ade69687e7b3bbe5b7a5e4bd9ce5a4b1e8afafe4b99fe6b2a1e69c89e689bee588b00ae8bf99e6a0b7e79a84e59b9ee5a48de7bb93e69e9ce4bba4e68891e5a4b1e69c9be38082e4bd86e6af95e4b89ae8aebae69687e68f90e4baa4e58db3e5b086e688aae6ada2efbc8ce68891e58faae883bde58588e5b086e5bf83e6809de694bee59ca8e8aebae69687e58699e4bd9ce4b88ae380820ae4b8890a34e69c883232e697a5e6999ae4b88ae58d81e4b880e782b9e5b7a6e58fb3efbc8ce8be85e5afbce59198e7aa81e784b6e7bb99e68891e68993e69da5e794b5e8af9defbc8ce4bd86e59ba0e4b8bae697b6e997b4e5b7b2e6999aefbc8ce68891e5b9b6e6b2a1e69c89e68ea5e588b0e38082e5878ce699a8e4b880e782b9efbc8ce8be85e5afbce59198e5928ce6af8de4bab2e7aa81e784b6e69da5e588b0e68891e79a84e5aebfe8888defbc8ce5bcbae8a18ce5b086e68891e58fabe98692efbc8ce8a681e6b182e68891e588a0e999a4e6898be69cbae38081e794b5e88491e4b8ade68980e69c89e4b88ee4bfa1e681afe585ace5bc80e4ba8be4bbb6e79bb8e585b3e79a84e8b584e69699efbc8ce5b9b6e4ba8ee5a4a9e4baaee5908ee588b0e5ada6e5b7a5e88081e5b888e5a484e4bd9ce587bae4b9a6e99da2e4bf9de8af81e4b88de5868de4bb8be585a5e6ada4e4ba8be38082e69c89e5908ce6a5bce5b182e79a84e5908ce5ada6e58fafe4bba5e4bd9ce8af81e38082e99a8fe5908eefbc8ce68891e8a2abe5aeb6e995bfe5b8a6e59b9ee5aeb6e4b8adefbc8ce79baee5898de697a0e6b395e8bf94e6a0a1e380820ae68891e5928ce6af8de4bab2e983bde5bdbbe5a49ce69caae79ca0e38082e5ada6e6a0a1e59ca8e88194e7b3bbe6af8de4bab2e697b6e6adaae69bb2e4ba8be5ae9eefbc8ce5afbce887b4e6af8de4bab2e58f97e588b0e8bf87e5baa6e6838ae59093e38081e68385e7bbaae5b4a9e6ba83e38082e59ba0e4b8bae5ada6e6a0a1e5bcbae8a18ce697a0e79086e79a84e4bb8be585a5efbc8ce68891e5928ce6af8de4bab2e585b3e7b3bbe587a0e4b98ee7a0b4e8a382e38082e5ada6e999a2e79baee5898de79a84e8a18ce58aa8e5b7b2e7aa81e7a0b4e5ba95e7babfefbc8ce68891e6849fe588b0e68190e683a7e8808ce99c87e68092e380820ae794b3e8afb7e4bfa1e681afe585ace5bc80e4bd95e7bdaae4b98be69c89efbc9fe68891e6b2a1e69c89e5819ae99499e4bbbbe4bd95e4ba8befbc8ce4b99fe4b88de4bc9ae5908ee68294e69bbee7bb8fe68f90e4baa4e3808ae4bfa1e681afe585ace5bc80e794b3e8afb7e8a1a8e3808befbc8ce8a18ce4bdbfe68891e4bd9ce4b8bae58c97e5a4a7e5ada6e7949fe79a84e58589e88da3e69d83e588a9e380820ae4ba8ce58d81e5b9b4e5adbae68595e68385e6b7b1efbc8ce68891e788b1e68891e79a84e6af8de4bab2e38082e99da2e5afb9e5a5b9e79a84e59a8ee59595e7979be593ade38081e887aae68987e880b3e58589e38081e4b88be8b7aae8afb7e6b182e38081e4bba5e887aae69d80e79bb8e88381efbc8ce68891e79a84e58685e5bf83e59ca8e6bbb4e8a180e38082e59ca8e5a5b9e79a84e59380e6b182e4b88be68891e58faae883bde69a82e697b6e59b9ee588b0e5aeb6e4b8adefbc8ce4bd86e58e9fe58899e99da2e5898de98080e697a0e58fafe98080efbc8ce5a6a5e58d8fe4b88de883bde8a7a3e586b3e4bbbbe4bd95e997aee9a298efbc8ce68891e588abe697a0e4bb96e6b395efbc8ce58faae69c89e58699e4b88be8bf99e7af87e5a3b0e6988eefbc8ce99988e8bfb0e58e9fe5a794e380820ae68385e7bbaae6bf80e58aa8efbc8ce8afb7e5a4a7e5aeb6e58e9fe8b085e68891e79a84e8afade697a0e4bca6e6aca1e380820ae59b9b0ae59ca8e6ada4efbc8ce68891e6ada3e5bc8fe59091e58c97e4baace5a4a7e5ada6e5a496e59bbde8afade5ada6e999a2e68f90e587bae4bba5e4b88be8af89e6b182efbc9a0a31e38081e58c97e4baace5a4a7e5ada6e5a496e59bbde8afade5ada6e999a2e5ba94e585ace5bc80e4b9a6e99da2e8afb4e6988ee8b68ae8bf87e68891e59091e5aeb6e995bfe696bde58e8be38081e5878ce699a8e588b0e5aebfe8888de5bcbae8a18ce7baa6e8b088e68891e38081e8a681e6b182e68891e588a0e999a4e794b3e8afb7e4bfa1e681afe585ace5bc80e4b880e4ba8be79a84e79bb8e585b3e8b584e69699e68980e4be9de68daee79a84e8a784e7aba0e588b6e5baa6efbc8ce5afb9e6ada4e8bf87e7a88be4b8ade8bf9de6b395e8bf9de8a784e6938de4bd9ce4ba88e4bba5e6988ee7a1aeefbc8ce5b9b6e98787e58f96e68eaae696bde981bfe5858de6ada4e7b1bbe4ba8be4bbb6e5868de6aca1e58f91e7949fe380820a32e38081e58c97e4baace5a4a7e5ada6e5a496e59bbde8afade5ada6e999a2e5ba94e7ab8be58db3e5819ce6ada2e4b880e58887e5afb9e68891e5aeb6e4babae79a84e696bde58e8be8a18ce4b8baefbc8ce59091e68891e5b7b2e7bb8fe981ade58f97e6838ae59093e79a84e6af8de4bab2e6ada3e5bc8fe98193e6ad89e5b9b6e6be84e6b885e4ba8be5ae9eefbc8ce5b8aee58aa9e4bfaee5a48de59ba0e6ada4e4ba8be5afbce887b4e79a84e5aeb6e5baade7b4a7e5bca0e585b3e7b3bbe380820a33e38081e58c97e4baace5a4a7e5ada6e5a496e59bbde8afade5ada6e999a2e5bf85e9a1bbe585ace5bc80e4b9a6e99da2e4bf9de8af81e6ada4e4ba8be4b88de4bc9ae5afb9e69cace4babae6af95e4b89ae4b880e4ba8be4baa7e7949fe5bdb1e5938defbc8ce5b9b6e4b88de4bc9ae5868de5b0b1e6ada4e4ba8be7bba7e7bbade5b9b2e689b0e68891e79a84e8aebae69687e58699e4bd9ce8bf9be7a88be380820a34e38081e58c97e4baace5a4a7e5ada6e5a496e59bbde8afade5ada6e999a2e8b49fe8b4a3e6b688e999a4e6ada4e4ba8be5afb9e69cace4babae5ada6e4b89ae38081e69caae69da5e5b0b1e4b89ae5928ce5aeb6e4babae79a84e585b6e4bb96e4b880e58887e4b88de889afe5bdb1e5938de380820a35e38081e58c97e4baace5a4a7e5ada6e5a496e59bbde8afade5ada6e999a2e5ba94e6988ee7a1aee5b0b1e4bba5e4b88ae8af89e6b182e8bf9be8a18ce585ace5bc80e4b9a6e99da2e59b9ee5a48defbc8ce7bb99e585b3e6b3a8e6ada4e4ba8be79a84e5a4a7e5aeb6e4b880e4b8aae4baa4e4bba3e380820ae68891e5b086e4bf9de79599e9809ae8bf87e6b395e5be8be6898be6aeb5e8bf9be4b880e6ada5e8bfbde7a9b6e79bb8e585b3e4b8aae4babae5928ce58d95e4bd8de8b4a3e4bbbbe79a84e4b880e58887e69d83e588a9efbc8ce58c85e68bace4bd86e4b88de99990e4ba8ee59091e58c97e4baace5a4a7e5ada6e5928ce4b88ae7baa7e4b8bbe7aea1e983a8e997a8e4b8bee68aa5e5a496e59bbde8afade5ada6e999a2e4b8a5e9878de8bf9de58f8de6a0a1e7baaae79a84e8a18ce4b8bae380820ae58c97e4baace5a4a7e5ada6e5a496e59bbde8afade5ada6e999a23134e7baa7e69cace7a791e7949fe5b2b3e698950a32303138e5b9b434e69c883233e697a50a");
//
//        try {
//            System.out.println(new String(bytes,"UTF-8"));
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }

        /**
         * 将字符串str转为Unicode字节码（非任何一种编码的字节码存储，硬要说的话，就是UTF-16）
         */
//        System.out.println(strToBinstr("我爱你"));

        /**
         * 等待读取用户从控制台输入信息，如果遇到回车则说明读取完毕，不然一直会阻塞。-可以作为终止程序的手段
         */
//        System.out.println("press Enter to quit");
//        try {
//            System.in.read();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        // exec.shutdownNow();

        /**
         * assert语句
         */
//        assert !true:"already serving: ";

        /**
         * 使用List等作为HashMap的键，因为List不是不可变对象，所以它的hashcode是会变化的，导致使用索引查值出现混乱
         */
//        List list = new ArrayList();
//        Map map = new HashMap();
//        System.out.println(list.hashCode());
//        map.put(list,"a");
//        list.add(1);
//        System.out.println(list.hashCode());
//        map.put(list,"b");
//        Set<Entry> entrySet = map.entrySet();
//        for(Iterator<Entry> it = entrySet.iterator(); it.hasNext();){
//            System.out.println(it.next());
//        }
//
//        System.out.println(map.get(list));
//        List list1 = new ArrayList<>();
//        System.out.println(list1.hashCode());
//        System.out.println(map.get(list1));

//        String str = "1 - 20　共 1,120 条";
//        String rgex = "共(.*?)条";
//
//        Pattern pattern = Pattern.compile(rgex);// 匹配的模式
//        Matcher m = pattern.matcher(str);
//        if(m.find()){
//            System.out.println(m.group(1).trim().replaceAll(",",""));
//            System.out.println();
//        }

        /**
         * 快捷创建目录和文件的方式（判断语句中进行目录创建，使用FileWriter自动创建文件）
         */
//        File eFile = new File("D:\\test\\test1\\test.txt");
//        if (!eFile.getParentFile().exists() && !eFile.getParentFile().mkdirs()){
//            System.out.println("进入if判断语句");
//        }
//        try (
//            BufferedWriter exportWriter = new BufferedWriter(new FileWriter(eFile));
//            ) {
//
//        }catch (IOException e){
//
//        }

        /**
         * 某些容器的Iterator提供remove()方法，可以提供比容器本身的remove()方法更好的使用效果，比如Set一边遍历一边删除元素。
         */
//        List<String> set = new ArrayList<>();
//        set.add("a");set.add("b");set.add("c");set.add("d");set.add("e");set.add("g");
////        //可以遍历间隔删除元素
//        Iterator iterator = set.iterator();
//        while(iterator.hasNext()){
//            iterator.next();
//            if (iterator.hasNext()){
//                iterator.next();
//            }
//            iterator.remove();
//        }
//
//        int size = set.size();
//        for (int i = 1;i<size;i=i+1){
//            set.remove(i);
//            size = set.size();
//        }

        //抛出ConcurrentModificationException异常
//        for (String str:set) {
//            System.out.println(Arrays.toString(set.toArray()));
//            set.remove(str);
//            System.out.println(Arrays.toString(set.toArray()));
//        }
//
//        System.out.println(Arrays.toString(set.toArray()));

//        Colored colored = new Colored();
//        System.out.println(colored.innerClass().getOutName());

//        Object[] strings = (Number[])new Integer[10];
//        strings[1] = 1;
//        strings[2] = "a";
//        System.out.println(strings[1]);
//
//        Fanxing<String> fanxing = new Fanxing<>();
//        Object[] strings1 = fanxing.getTs();
//        strings1[1]= "a";
//        System.out.println(strings1[1]);

        /**
         * 真正理解java的方法参数传递
         */
//        String[] array2 = new String[] {"huixin"};
//        System.out.println("调用reset方法前array中的第0个元素的值是:" + array2[0]);
//        reset(array2);
//        System.out.println("调用reset方法后array中的第0个元素的值是:" + array2[0]);
//
//        Dinner array = new Dinner();
//        array.setName("aaa");
//        System.out.println("调用reset方法前array中的第0个元素的值是:" + array + " " + array.getName());
//        reset(array);
//        System.out.println("调用reset方法后array中的第0个元素的值是:" + array + " " + array.getName());
//
//        String array1 = new String("aaa");
//        System.out.println("调用reset方法前array中的第0个元素的值是:" + array1);
//        reset(array1);
//        System.out.println("调用reset方法后array中的第0个元素的值是:" + array1);


//        Ca cc = new Ca();
//        Type[] ts = cc.getClass().getGenericInterfaces();
//        Type t;
//        Type[] as;
//        ParameterizedType p;
//        Class<?> c = cc.getClass();
//        if (ts != null) {
//            for (int i = 0; i < ts.length; ++i) {
//                if (((t = ts[i]) instanceof ParameterizedType) &&
//                    ((p = (ParameterizedType)t).getRawType() ==
//                        Comparable.class) &&
//                    (as = p.getActualTypeArguments()) != null &&
//                    as.length == 1 && as[0] == c) // type arg is c
//                    System.out.println( c);
//            }
//        }


        /**
         * 通过allowCoreThreadTimeOut()，让线程池自动在所有创建的线程中的任务执行完毕后关闭，而不用显示调用shutdown()。
         */
//        ThreadPoolExecutor pool = new ThreadPoolExecutor(20, 20,
//            1L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(100), new ThreadPoolExecutor.CallerRunsPolicy());
//
//        System.out.println(pool.getActiveCount());

//        pool.allowCoreThreadTimeOut(true);

        // 开启50个线程来处理
//        for (int i = 0; i < 50; i++) {
//            pool.submit(new RunImpl(i));
//        }
//        pool.shutdown();
//        System.out.println(pool.getActiveCount());

//            boolean loop = true;
//            do {    //等待所有任务完成
//                loop = !pool.awaitTermination(2, TimeUnit.SECONDS);  //阻塞，直到线程池里所有任务结束
////                loop = !pool.isTerminated();//调用了shutdown()会起作用，allowCoreThreadTimeOut()没用
//            } while(loop);
//        System.out.println(pool.getActiveCount());

        /**
         * 获得当前java虚拟机启动的系统参数，并可通过setProperty()设置自定义的系统参数
         * Either System.setProperty or use the -Dname=value flag when you start the JVM
         */
//        System.setProperty("LOG_FILE","my.log");
//        Properties properties = System.getProperties();
//        System.out.println(properties.size());
//        for (Object o : properties.keySet()) {
//            System.out.println("" + o + "=" + properties.get(o));
//        }

        /**
         * 使用fasterxml.jackson的注解配合Filter过滤器实现对bean序列化属性的选择
         * https://www.cnblogs.com/tomcatandjerry/p/9139976.html
         *
         * 使用net.sf.json-lib的JsonConfig配合JSONObject实现对bean序列化属性的选择
         * fromObject方法通过反射机制去寻找bean类即bean.class，如果bean类为内部类，
         * 所以找不到该类，也就没有类属性的getter和setter方法了。
         * 解决方案：把bean类提取出来作为单独的一个类。——算是不足之处吧
         */
//        Dinner dinner = new Dinner("Love",1,"Shanghai",LocalDateTime.of(2018,7,7,14,30),222);
//
//
//        ObjectMapper mapper = new ObjectMapper();
//        String[] beanProperties = new String[]{"name","mount","place"};
//        String filterName = "myFilter";//需要跟Dinner类上的注解@JsonFilter("myFilter")里面的一致
//        FilterProvider filterProvider = new SimpleFilterProvider()
//            .addFilter(filterName, SimpleBeanPropertyFilter.serializeAllExcept(beanProperties));
//        //serializeAllExcept 表示序列化全部，除了指定字段
//        //filterOutAllExcept 表示过滤掉全部，除了指定的字段
//        mapper.setFilterProvider(filterProvider);
//        try {
//            System.out.println(mapper.writeValueAsString(dinner));
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//
//        JsonConfig jsonConfig = new JsonConfig();
//        Dinner dinner = new Dinner("Love",1,"Shanghai",LocalDateTime.of(2018,7,7,14,30),222);
//        jsonConfig.setExcludes(new String[]{"name","mount","place"});
//        JSONObject jsonObject = JSONObject.fromObject(dinner,jsonConfig);
//        System.out.println(jsonObject.toString());

        /**
         * 使用jdbc连接mysql数据库
         */
//        Connection conn = null;
//        Statement stmt = null;
//        ResultSet rset = null;
//        try {
//            Class.forName("com.mysql.cj.jdbc.Driver");
//            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mytest?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC","root","");
////            conn.setAutoCommit(false);//开启事务
//
//            DatabaseMetaData dbmd = conn.getMetaData();
//            System.out.println("-----------Catalogs-------------");
//            rset = dbmd.getCatalogs();
//            while (rset.next())
//                System.out.println(rset.getString("TABLE_CAT"));
//            rset = dbmd.getSchemas();
//            System.out.println("------------Schemas-------------");
//            while (rset.next())
//                System.out.println(rset.getString("TABLE_SCHEM"));
//            System.out.println("--------------------------------");
//            rset = dbmd.getColumns("mytest",null,"%m%","%");
//            while (rset.next())
//                System.out.println(rset.getString("COLUMN_NAME")+" "+ rset.getString("TYPE_NAME"));
//
//
//
//            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);//创建可回滚对数据库改变敏感的可更新结果集
//            rset = stmt.executeQuery("SELECT * FROM classroom");
//            rset.getMetaData();
//            while (rset.next()){
//                System.out.println(rset.getString("building"));
//                rset.updateString("building","Obama");
//                rset.updateRow();
//                System.out.println(rset.getString("building"));
//            }
////            stmt.executeUpdate("INSERT INTO time(date,time,time_stamp) VALUES('2018-07-25','16:25:00','2018-07-25 16:25:00') ");
//
////            conn.commit();//try的最后提交事务
//
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }catch (SQLException e) {
//            e.printStackTrace();
//        }
//        finally {
//            if(rset != null)
//                try {
//                    rset.close();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            if(stmt != null)
//                try {
//                    stmt.close();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            if(conn != null)
//                try {
//                    conn.close();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//        }


        /**
         * 使用Gson将Map转为JsonObject
         */
//        LinkedHashMap linkedHashMap =new LinkedHashMap();
//        linkedHashMap.put("memo","xxxxx");
//        linkedHashMap.put("result","{\"alipay_trade_app_pay_response\":{\"code\":\"10000\",\"msg\":\"Success\",\"app_id\":\"2014072300007148\",\"out_trade_no\":\"081622560194853\",\"trade_no\":\"2016081621001004400236957647\",\"total_amount\":\"0.01\",\"seller_id\":\"2088702849871851\",\"charset\":\"utf-8\",\"timestamp\":\"2016-10-11 17:43:36\"},\"sign\":\"NGfStJf3i3ooWBuCDIQSumOpaGBcQz+aoAqyGh3W6EqA/gmyPYwLJ2REFijY9XPTApI9YglZyMw+ZMhd3kb0mh4RAXMrb6mekX4Zu8Nf6geOwIa9kLOnw0IMCjxi4abDIfXhxrXyj********\",\"sign_type\":\"RSA2\"}");
//        linkedHashMap.put("resultStatus","9000");
//        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
//        JsonObject result = gson.fromJson(gson.toJson(linkedHashMap),JsonObject.class);
//        System.out.println(result);

        /**
         * 使用Gson的TypeAdapter自定义解析json时对象的属性的具体处理逻辑
         * 参考：https://www.jianshu.com/p/8cc857583ff4
         * 参考：https://blog.csdn.net/sunrainamazing/article/details/80974993
         */
//        ChargeInfo chargeInfo = new ChargeInfo();
//        chargeInfo.setProductId("2028597139,2028597140");
//        chargeInfo.setProportion1("30");
//        chargeInfo.setProportion2("30");
//        chargeInfo.setProportion3("30");
//
//        ContentMGDO contentMGDO = new ContentMGDO();
//        List<ChargeInfo> list = new ArrayList<>();
//        list.add(chargeInfo);
//        contentMGDO.setChargeList(list);
//
//        Gson gson = new GsonBuilder().registerTypeAdapter(ChargeInfo.class,new chargeInfoTypeAdapter()).create();
//        String message = gson.toJson(contentMGDO, ContentMGDO.class);
//        System.out.println(message);


        /**
         * 字符缓存
         */
//        String a = "a";
//        String b = "b";
//        String c = "ab";
//        String d = "a"+"b";
//        System.out.println((a+b)==c); //true or false
//        System.out.println(c==d); //true or false

        /**
         * 构造可复制的对象，特别注意当对象中的属性不是基本类型而是引用对象类型时，需要自定义重写Object的clone()方法实现深复制
         */
//        Professor p1 = new Professor();
//        p1.setName("Professor Zhang");
//        p1.setAge(30);
//
//        Student s1 = new Student();
//        s1.setName("xiao ming");
//        s1.setAge(18);
//        s1.setProfessor(p1);
//
//        System.out.println(s1);
//
//        try {
//            Student s2 = s1.myClone();
//            Professor p2 = s2.getProfessor();
//            p2.setName("Professor Li");
//            p2.setAge(45);
//            s2.setProfessor(p2);
//            System.out.println("复制后的：s1 = " + s1);
//            System.out.println("复制后的：s2 = " + s2);
//        } catch (CloneNotSupportedException e) {
//            e.printStackTrace();
//        }

        /**
         * 菱形图形输出
         */
//        rhomb(20);

        /**
         * 获得二进制各个位的 1 or 0
         */
//        byte b = 67;
//        System.out.println(b & 1);
//        System.out.println(b>>>1 & 1);
//        System.out.println(b>>>2 & 1);

        /**
         * 插入排序
         */
//        int[] ints = new int[]{5,2,3,0,4,7,6,8,9,1};
//        int[] tem = new int[ints.length];
//        for (int i=0;i<ints.length;i++){
//            tem[i]=ints[i];
//            for (int j=i;j>=0;j--){
//                if(j-1>=0 && tem[j-1]>tem[j]){
//                    int temp = tem[j];
//                    tem[j] = tem[j-1];
//                    tem[j-1] = temp;
//                }
//            }
//        }
//        System.out.println(Arrays.toString(tem));

        /**
         * 冒泡排序
         */
//        int[] ints = new int[]{5,2,3,0,4,7,6,8,9,1};
//        for (int i=1;i<ints.length;i++){
//            int j = i-1;
//            while (j>=0 && ints[j]>ints[i]){
//                int temp = ints[j];
//                ints[j] = ints[i];
//                ints[i] = temp;
//                j--;
//                i--;
//            }
//        }
//        System.out.println(Arrays.toString(ints));
        /**
         * 父子类的同名属性、相同的静态方法不具有多态性，即属性值和静态方法是看引用而不是看实际的引用对象。
         */
//        Dinner dinner = new Lunch();
//        System.out.println(dinner.print());
//        System.out.println(dinner.attr);
//        dinner.method();

        /**
         * 使用set去重
         */
//        List<String> stringList = Arrays.asList("2028597139,2028597139,2028597140".split(","));
//        Set<String> stringSet =stringList.stream().collect(Collectors.toSet());
//
//        System.out.println(Arrays.toString(stringSet.stream().toArray(String[]::new)));
//
////        String[] strings = (String[]) stringSet.toArray();//toArray()方法真的把set中的元素全部变为Object了，而不仅仅是向上转型
//        String[] strings = stringSet.stream().toArray(String[]::new);
//        String productIds = String.join(",",strings);
//        System.out.println(productIds);


        /**
         * lambda表达式求对象collection的统计信息
         */
//        CtFile ctFile1 = new CtFile();
//        ctFile1.setContentDuration("10");
//        CtFile ctFile2 = new CtFile();
//        ctFile2.setContentDuration("20");
//        CtFile ctFile3 = new CtFile();
//        ctFile3.setContentDuration("15");
//        CtFile ctFile4 = new CtFile();
//        List<CtFile> ctFiles = new ArrayList<>();
//        ctFiles.add(ctFile1);
//        ctFiles.add(ctFile2);
//        ctFiles.add(ctFile3);
//        ctFiles.add(ctFile4);
//        try {
//            IntSummaryStatistics intSummaryStatistics = ctFiles.stream().mapToInt(ctfile -> Integer.valueOf(ctfile.getContentDuration())).summaryStatistics();
//            int minDuration = intSummaryStatistics.getMin();
//            System.out.println(minDuration);
//        }catch (Exception e){
//            log.error("setShortVideo occurred some problem: ",e);
//        }


//        LogUtil logUtil = new LogUtil(Colored.class);
//        logUtil.logInfo( LogActionTypeEnum.Create,"1985129876", "abc123", "content", "taskId17578", "ctaskId1423", "error detail");

        /**
         * Unicode和字符互转
         */
//        System.out.println(unicode2String("\\u007c"));

//        LIST.add("b");
//        System.out.println(Arrays.toString(LIST.toArray()));
//        new Colored<>();

        System.out.println(StringUtils.arrayToDelimitedString(new Integer[]{1,2,3,4,5}, "; "));


    }

    static final List<String> LIST = Collections.unmodifiableList(new ArrayList<String>(){
        {
            add("a");
        }
    });

    ClassFactory factory = new ClassFactory<String>(){
        {
            System.out.println(create());
        }

        @Override
        public String create() {
            return "aaa";
        }
    };

    public static String string2Unicode(String string) {
        StringBuffer unicode = new StringBuffer();
        for (int i = 0; i < string.length(); i++) {
            // 取出每一个字符
            char c = string.charAt(i);
            // 转换为unicode
            unicode.append("\\u" + Integer.toHexString(c));
        }
        return unicode.toString();
    }

    public static String unicode2String(String unicode) {
        StringBuffer string = new StringBuffer();
        String[] hex = unicode.split("\\\\u");
        for (int i = 1; i < hex.length; i++) {
            // 转换出每一个代码点
            int data = Integer.parseInt(hex[i], 16);
            // 追加成string
            string.append((char) data);
        }
        return string.toString();
    }


//    static class chargeInfoTypeAdapter extends TypeAdapter {
//
//        @Override
//        public void write(JsonWriter out, Object value) throws IOException {
//            ChargeInfo chargeInfo = (ChargeInfo)value;
//
//            List<String> productIds = Arrays.asList(chargeInfo.getProductId().split(","));
//            for(String productId:productIds){
//                out.beginObject();
//                out.name("productId").value(productId);
//                out.name("proportion1").value(chargeInfo.getProportion1());
//                out.name("proportion2").value(chargeInfo.getProportion2());
//                out.name("proportion3").value(chargeInfo.getProportion3());
//                out.endObject();
//            }
//
//
//        }
//
//        @Override
//        public Object read(JsonReader in) throws IOException {
//            return null;
//        }
//    }

        public static String formatStr(String str,int number){
        StringBuilder stringBuilder = new StringBuilder();
        for(int i=0;i<number;i++){
             stringBuilder.append(str);
        }
        return stringBuilder.toString();
    }

    public static void rhomb(int length){
        int spaceMax = (length-1)/2;
        int levelMax = (length+1)/2;
        for(int i=1,j=spaceMax;i<=levelMax;i++,j--){
            System.out.println(formatStr(" ",j)+formatStr("*",2*i-1));
        }
        for(int i=levelMax-1,j=1;i>0;i--,j++){
            System.out.println(formatStr(" ",j)+formatStr("*",2*i-1));
        }
    }

    public static Integer test() {
        Optional<Integer> optional = Optional.ofNullable(null);
        try {
            return optional.get();
        }catch (NoSuchElementException e){
            return null;
        }

    }

    public static void reset(String[] param) {
        param[0] = new String("hello, world!"); //可以理解为改变param这个数组引用（句柄）的对象的属性，即数组中第一个元素的指向的对象
    }

    public static void reset(Dinner param) {
        param.setName("bbb"); //改变了传递进来的句柄指向的对象的属性，所以方法返回后改变了array指向的对象的属性
        param = new Dinner(); //参数引用（句柄）指向另一个对象，返回方法后，方法中的new Dinner()失去了唯一的param句柄，会被垃圾回收器回收。这里有2个对象，reset()方法外的Dinner对象，reset()方法内的Dinner对象；2个变量，array获得外部Dinner的句柄，param获得内部Dinner的句柄。
    }

    public static void reset(String param) {
        param = new String("bbb");    }

}

class RunImpl implements  Runnable{

    int i;

    public RunImpl(int i) {
        this.i = i;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName()+": " + i);
    }
}

interface X {}
interface Y {}
class Ca implements X{}
class Cb implements Y{}
class Cc extends Ca{}

class Fanxing<T>{
    T[] ts = (T[])new Object[10];

    public T[] getTs(){
        return ts;
    }
}

interface ClassFactory<T> {
    T create();
}

class Part {
    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    static List<ClassFactory<? extends Part>> partFactories = new ArrayList<>();

    static {
        partFactories.add(new FuelFilter.Factory());
        partFactories.add(new AirFilter.Factory());
    }
}

class FuelFilter extends Part {
    public static class Factory implements ClassFactory<FuelFilter> {
        @Override
        public FuelFilter create() {
            return new FuelFilter();
        }
    }
}

class AirFilter extends Part {
    public static class Factory implements ClassFactory<AirFilter> {
        @Override
        public AirFilter create() {
            return new AirFilter();
        }
    }
}





