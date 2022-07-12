package 最短路径;

import java.util.Scanner;
import java.util.Vector;

/**
 * 题目分析：
 * 1.有向图(one-way),标志1表示单向，标志0表示双向
 * 2.顶点序号从0到n-1
 * 3.找最短路径--->最短路径不唯一的时候，看最短时间
 * 4.找最短时间--->最短时间不唯一的时候，看经过的最少节点
 * 5.最短时间不一定是在最短路径中产生
 *
 * 思路：
 * 1.两次dijkstra,一次把路径作为边权，一次把时间作为边权
 * 2.还是得用dijkstra+dfs
 */
public class PAT_A1111 {

    final static int MAXN=510;
    final static int INF=1000001;
    static int n;//节点数
    static int m;//边数
    static int start;
    static int des;
    static int[][] g=new int[MAXN][MAXN];//记录距离
    static int[][] time=new int[MAXN][MAXN];//记录时间

    //dijkstra:
    static int[] d=new int[MAXN];
    static boolean[] vis=new boolean[MAXN];
    static Vector<Integer>[] pres=new Vector[MAXN];

    //dfs:
    static Vector<Integer> tempPath= new Vector<>();
    static Vector<Integer> optPath= new Vector<>();//记录最短路径
    static int shortTime=INF;//最短路径下的最短时间
    static int shortDistance=0;
    //dfs2:
    static Vector<Integer> tempPath2=new Vector<>();
    static Vector<Integer> optPath2=new Vector<>();//记录最短时间
    static int leastVertex=INF;//路径上最少节点数
    static int sTime=0;


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        n= scanner.nextInt();
        m= scanner.nextInt();
        for(int i=0;i<MAXN;i++){
            for(int j=0;j<MAXN;j++){
                g[i][j]=INF;
                time[i][j]=INF;
            }
        }
        for (int i=0;i<m;i++){
            int v1= scanner.nextInt();
            int v2= scanner.nextInt();
            int isOneWay= scanner.nextInt();//1则是单向，0则是双向
            int tempLength= scanner.nextInt();
            int tempTime= scanner.nextInt();
            g[v1][v2]=tempLength;
            time[v1][v2]= tempTime;
            if(isOneWay!=1){//双向
                g[v2][v1]=tempLength;
                time[v2][v1]= tempTime;
            }
        }
        start= scanner.nextInt();
        des= scanner.nextInt();
        dijkstra(start);
        dfs(des);
        for(int i=0;i<MAXN;i++){
            vis[i]=false;
        }
        shortDistance=d[des];
        //将边权由距离换成时间
        for(int i=0;i<MAXN;i++){
            for(int j=0;j<MAXN;j++){
                g[i][j]=time[i][j];
            }
        }
        dijkstra(start);
        dfs2(des);
        sTime=d[des];
        boolean flag=true;
        if(optPath.size()==optPath2.size()){
            for(int i=0;i<optPath.size();i++){
                if(optPath.get(i)!=optPath2.get(i)){
                    flag=false;
                    break;
                }
            }
        }else {
            flag=false;
        }

        if(flag){
            System.out.print("Distance"+" = "+shortDistance+"; ");
            System.out.print("Time"+" = "+sTime+": ");
            for(int i=optPath.size()-1;i>=0;i--){
                if(i==0){
                    System.out.print(optPath.get(i));
                }else{
                    System.out.print(optPath.get(i)+" -> ");
                }
            }
        }else{
            System.out.print("Distance"+" = "+shortDistance+": ");
            for(int i=optPath.size()-1;i>=0;i--){
                if(i==0){
                    System.out.println(optPath.get(i));
                }else{
                    System.out.print(optPath.get(i)+" -> ");
                }
            }
            System.out.print("Time"+" = "+sTime+": ");
            for(int i=optPath2.size()-1;i>=0;i--){
                if(i==0){
                    System.out.print(optPath2.get(i));
                }else{
                    System.out.print(optPath2.get(i)+" -> ");
                }
            }
        }
    }

    public static void dfs2(int v){
        if(v==start){
            tempPath2.add(v);
            if(tempPath2.size()<leastVertex){
                leastVertex=tempPath2.size();
                optPath2.clear();
                for (Integer integer : tempPath2) {
                    optPath2.add(integer);
                }
            }
            tempPath2.remove((Object)v);
        }
        tempPath2.add(v);
        for(int i=0;i<pres[v].size();i++){
            dfs2(pres[v].get(i));
        }
        tempPath2.remove((Object)v);
    }

    public static void dfs(int v){
        if(v==start){
            tempPath.add(v);
            int tempTime=0;
            for(int i=tempPath.size()-1;i>=1;i--){
                tempTime+=time[tempPath.get(i)][tempPath.get(i-1)];
            }
            if(tempTime<shortTime){
                shortTime=tempTime;
                optPath.clear();
                for (Integer integer : tempPath) {
                    optPath.add(integer);
                }
            }
            tempPath.remove((Object)v);
            return;
        }
        tempPath.add(v);
        for(int i=0;i<pres[v].size();i++){
            dfs(pres[v].get(i));
        }
        tempPath.remove((Object)v);
    }

    public static void dijkstra(int start){
        for(int i=0;i<MAXN;i++){
            d[i]=INF;
            pres[i]=new Vector<>();
        }
        d[start]=0;
        for(int i=0;i<n;i++){
            int u=-1;
            int MIN=INF;
            for (int j=0;j<n;j++){
                if(vis[j]==false&&d[j]<MIN){
                    u=j;
                    MIN=d[j];
                }
            }
            if(u==-1) return;
            vis[u]=true;
            for(int v=0;v<n;v++){
                if(vis[v]==false&&g[u][v]!=INF){
                    if(d[u]+g[u][v]<d[v]){
                        d[v]=d[u]+g[u][v];
                        pres[v].clear();
                        pres[v].add(u);
                    }else if(d[u]+g[u][v]==d[v]){
                        pres[v].add(u);
                    }
                }
            }
        }
    }
}
