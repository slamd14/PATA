package 最短路径;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * 题目分析：
 * 1.两类节点：汽油站、居民点
 * 2.汽油站是起点，从起点开始要能访问到所有的居民点
 * 3.四个要求：1.从起点开始要能访问到所有的居民点 2.首先选择距离各个居民点最短距离最大的站点 3.如果有多个则smallest average，而居民点个数相同，便是要求到达各居民点的最短路径总和 4.如果满足1. 2.的路径有多条，那么选取汽油站编号最小的那个
 * 4.输出：No Solution/选取的汽油站+选取的汽油站到居民点的最小距离+平均距离
 * 5.居民编号由1到n，汽油站编号由g1到gm
 *
 * 思路：
 * 1.一个一个汽油站解决  有个问题：当解决当前汽油站的时候，其他汽油站也是在图中的，怎么表示?--->居民范围是[1,n],那不如把汽油站映射到n以后
 * 2.对于每个汽油站:dijkstra得到d[],如果所有d[]都小于汽油站服务范围，则满足要求1.，并累加d[]得到当前最短路径总和，如果当前最短路径总和小于或等于最短路径总和，则需要记录下汽油站编号+最小的d[]+最短路径总和
 * 3.遍历2.记录下的，得出结果
 *
 */
public class PAT_A1072 {

    final static int MAXN=1050;
    final static int INF=10000001;
    static int N;//居民点个数
    static int M;//候选汽油站个数
    static int K;//边数
    static int D;//汽油站最大服务距离
    static int[][]g=new int[MAXN][MAXN];
    static HashMap<String,Integer> stationToInt=new HashMap<>();
    static HashMap<Integer,String> intToStation=new HashMap<>();

    //dijkstra:
    static int[] d=new int[MAXN];
    static boolean[] vis=new boolean[MAXN];
    static int maxDistance=0;//维持各个站点距离其最近的村庄的最大距离
    static int shortPath=INF;//记录最短路径总和
    static int minIndex=-1;//记录站点序号

    public static void main(String[] args) throws IOException {
        InputStreamReader ir = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(ir);
        String line = br.readLine();
        String[] s = line.split(" ");
        N= Integer.parseInt(s[0]);
        M= Integer.parseInt(s[1]);
        K= Integer.parseInt(s[2]);
        D= Integer.parseInt(s[3]);
        for(int i=0;i<MAXN;i++){
            for(int j=0;j<MAXN;j++){
                g[i][j]=INF;
            }
        }
        for(int i=0;i<K;i++){
            String line1 = br.readLine();
            String[] s1 = line1.split(" ");
            String v1= s1[0];
            String v2= s1[1];
            int ver1=0;
            int ver2=0;
            if(v1.charAt(0)!='G'){
                ver1=Integer.parseInt(v1);
            }else{
                ver1=change(v1);
            }
            if(v2.charAt(0)!='G'){
                ver2=Integer.parseInt(v2);
            }else{
                ver2=change(v2);
            }
            int dis= Integer.parseInt(s1[2]);
            g[ver1][ver2]=dis;
            g[ver2][ver1]=dis;
        }
        br.close();
        //分别对每个汽油站dijkstra:

        HashMap<String, Integer> hp = new HashMap<String, Integer>();//key:汽油站编号 value：最小的d[](并且要是居民点)
        for(int i=N+1;i<=N+M;i++){//TODO 本来就是按汽油站序号遍历的，所以第一个记录的永远是index最小的汽油站，不需要集合存储满足条件的多个汽油站
            boolean tempFlag=true;
            int tempMin=INF;
            int tempPath=0;
            for(int j=0;j<MAXN;j++){//TODO 因为要用多次dijkstra,所以要消除上一次使用的影响
                vis[j]=false;
            }
            dijkstra(i);
            for(int j=1;j<=N;j++){
                if(d[j]>D){
                    tempFlag=false;
                    break;
                }
            }
            if(tempFlag==true){//所有居民点都在范围内
                for(int j=1;j<=N;j++){
                    if(d[j]<tempMin){
                        tempMin=d[j];
                    }
                    tempPath+=d[j];
                }
                if(tempMin>maxDistance){//TODO 题目要求的是:选取各个站点距离居民点的最短距离的最大值对应的站点
                    minIndex=i;
                    maxDistance=tempMin;
                    shortPath=tempPath;
                }else if(tempMin==maxDistance&&tempPath<shortPath){
                    minIndex=i;
                    shortPath=tempPath;
                }
            }
        }
        if(minIndex==-1){
            System.out.print("No Solution");
        }else{
            System.out.println(intToStation.get(minIndex));
            System.out.printf("%.1f %.1f",1.0*maxDistance,1.0*shortPath/N);
        }
    }

    public static void dijkstra(int start){
        for(int i=0;i<MAXN;i++){
            d[i]=INF;
        }
        d[start]=0;
        for(int i=0;i<N+M;i++){
            int u=-1;
            int MIN=INF;
            for(int j=1;j<=N+M;j++){
                if(vis[j]==false&&d[j]<MIN){
                    u=j;
                    MIN=d[j];
                }
            }
            if(u==-1) return;
            vis[u]=true;
            for(int v=1;v<=N+M;v++){
                if(vis[v]==false&&g[u][v]!=INF){
                    if(d[u]+g[u][v]<d[v]){
                        d[v]=d[u]+g[u][v];
                    }
                }
            }
        }
    }

    public static int change(String station){
        if(stationToInt.containsKey(station)){
            return stationToInt.get(station);
        }else{
            int num=0;
            if(station.length()==3){//处理G10
                num=10;
            }else{
                char c = station.charAt(1);
                String s=""+c;
                num=Integer.parseInt(s);
            }
            stationToInt.put(station,num+N);
            intToStation.put(num+N,station);
            return num+N;
        }
    }
}
