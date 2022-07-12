package 最短路径;

import java.util.HashMap;
import java.util.Scanner;
import java.util.Vector;

/**
 * 题目分析:
 * 1.告诉起点，告诉终点，边权为cost，点权为happiness
 * 2.要求：最短路径条数 最小边权  最短路径有多条时--->最大点权   最大点权有多条--->最大点权平均值
 *
 * 思路：
 * 1.建立城市名字String与序号int的双向映射
 * 2.构建无向图，邻接矩阵存储边权
 * 3.dijkstra记录下所有最短路径
 * 4.dfs
 */
public class PAT_A1087 {

    final static int MAXN=210;
    final static int INF=10000001;
    static int n;//节点个数
    static int k;//边的个数
    static int start;//起点
    static HashMap<String,Integer> stringtoInt=new HashMap<>();
    static HashMap<Integer,String> intToString=new HashMap<>();
    static int currentVertexes=0;//节点序号从0开始
    static int[] weight=new int[MAXN];//点权
    static int[][] g=new int[MAXN][MAXN];

    //dijkstra:
    static int[] d=new int[MAXN];
    static boolean[] vis=new boolean[MAXN];
    static Vector<Integer>[] pres=new Vector[MAXN];

    //dfs:
    static Vector<Integer> tempPath=new Vector<>();
    static Vector<Integer> optPath=new Vector<>();
    static int maxSumvertex=0;
    static int maxSumvertexAvg=0;
    static int shortPaths=0;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        n= scanner.nextInt();
        k= scanner.nextInt();
        String s= scanner.next();
        start=change(s);
        weight[start]=0;
        for(int i=0;i<n-1;i++){
            String s1= scanner.next();
            int wei= scanner.nextInt();
            int v1 = change(s1);
            weight[v1]=wei;
        }
        for(int i=0;i<MAXN;i++){
            for(int j=0;j<MAXN;j++){
                g[i][j]=INF;//TODO 他妈的！！！用邻接矩阵存储图的时候不要他妈的忘记了给所有边权初始化为INF他妈的
            }
        }
        for(int i=0;i<k;i++){
            String s1= scanner.next();
            String s2= scanner.next();
            int dis= scanner.nextInt();
            int v1 = change(s1);
            int v2 = change(s2);
            g[v1][v2]=dis;
            g[v2][v1]=dis;
        }

        //dijkstra记录最短路径
        dijkstra(start);
        //dfs
        dfs(stringtoInt.get("ROM"));

        System.out.println(shortPaths+" "+d[stringtoInt.get("ROM")]+" "+maxSumvertex+" "+maxSumvertexAvg);
        for(int i= optPath.size()-1;i>=0;i--){
            if(i==0){
                System.out.print(intToString.get(optPath.get(i)));
            }else{
                System.out.print(intToString.get(optPath.get(i))+"->");
            }
        }

    }

    public static void dfs(int v){
        if(v==start){
            shortPaths++;
            int tempMaxSumVertex=0;
            int tempMaxSumVertexAVG=0;
            tempPath.add(v);
            for(int i=0;i<tempPath.size();i++){
                tempMaxSumVertex+=weight[tempPath.get(i)];
            }
            tempMaxSumVertexAVG=tempMaxSumVertex/(tempPath.size()-1);
            if(tempMaxSumVertex>maxSumvertex){
                maxSumvertex=tempMaxSumVertex;
                maxSumvertexAvg=tempMaxSumVertexAVG;//TODO 不要  只   记得更新maxSumvertex=tempMaxSumVertex  ,这个他妈的也要记得更新!!!!!!!!!因为tempMaxSumVertex>maxSumvertex的优先级更高，已经换情况了
                                                                            //TODO 因为他妈的maxSumvertexAvg是他妈的次标尺，主标尺更新了，其他的次标尺他妈的一定也要记得随着主标尺更新，不要忘记了
                optPath.clear();
                for (Integer integer : tempPath) {
                    optPath.add(integer);
                }
            }else if(tempMaxSumVertex==maxSumvertex&&tempMaxSumVertexAVG>maxSumvertexAvg){
                maxSumvertexAvg=tempMaxSumVertexAVG;
                optPath.clear();
                for (Integer integer : tempPath) {
                    optPath.add(integer);
                }
            }
            tempPath.remove((Object)v);
            return;
        }
        tempPath.add(v);
        for (Integer integer : pres[v]) {
            dfs(integer);
        }
        tempPath.remove((Object)v);
    }

    public static void dijkstra(int s){
        for(int i=0;i<MAXN;i++){
            d[i]=INF;
            pres[i]=new Vector<>();
        }
        d[s]=0;
        for(int i=0;i<n;i++){
            int u=-1;
            int MIN=INF;
            for(int j=0;j<n;j++){
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

    public static int change(String s){
        if(stringtoInt.keySet().contains(s)){
            return stringtoInt.get(s);
        }else{
            int num=currentVertexes;
            stringtoInt.put(s,num);
            intToString.put(num,s);
            return currentVertexes++;
        }
    }
}
