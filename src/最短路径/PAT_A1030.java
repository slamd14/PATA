package 最短路径;

import java.util.Scanner;
import java.util.Vector;

/**
 * 1.双重标尺：最短路径+最小边权
 * 2.顶点序号0~n-1
 * 3.求双重标尺下的路径+距离+边权
 * 4.无向图
 *
 * 求解:
 * 1.邻接矩阵记录距离
 * 2.开二维数组记录边权
 * 3.dijkstra+dfs
 */
public class PAT_A1030 {

    static int n;//节点个数
    static int m;//边个数
    static int s;
    static int d;
    final static int MAXN=510;
    final static int INF=10000001;
    static int[][] g=new int[MAXN][MAXN];//距离
    static int[][] edgeWeight=new int[MAXN][MAXN];//边权

    //dijkstra:
    static int[] dis=new int[MAXN];
    static boolean[] vis=new boolean[MAXN];
    static Vector<Integer>[] pre=new Vector[MAXN];

    //dfs:
    static Vector<Integer> tempPath=new Vector<>();
    static Vector<Integer> optPath=new Vector<>();
    static int optValue=INF;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        n= scanner.nextInt();
        m= scanner.nextInt();
        s= scanner.nextInt();
        d= scanner.nextInt();
        for(int i=0;i<MAXN;i++){
            for(int j=0;j<MAXN;j++){
                g[i][j]=INF;
                edgeWeight[i][j]=INF;
            }
        }
        for(int i=0;i<m;i++){
            int v1= scanner.nextInt();
            int v2= scanner.nextInt();
            int distance= scanner.nextInt();
            int weight= scanner.nextInt();
            g[v1][v2]=distance;
            g[v2][v1]=distance;
            edgeWeight[v1][v2]=weight;
            edgeWeight[v2][v1]=weight;
        }
        //图初始化完毕
        //Dijkstra记录下所有最短路径:
        dijkstra(s);
        //dfs遍历所有最短路径，筛选出边权最小的:
        dfs(d);
        //输出结果:
        for(int i=optPath.size()-1;i>=0;i--){
            System.out.print(optPath.get(i)+" ");
        }
        System.out.print(dis[d]+" ");
        System.out.print(optValue);
    }

    public static void dfs(int des){
        if(des==s){
            tempPath.add(des);
            int tempValue=0;
            for (int i=0;i< tempPath.size()-1;i++){
                int v1 = tempPath.get(i);
                int v2 = tempPath.get(i+1);
                tempValue+=edgeWeight[v1][v2];
            }
            if(tempValue<optValue){
                optValue=tempValue;
                optPath.clear();
                for (Integer integer : tempPath) {//深拷贝
                    optPath.add(integer);
                }
            }
            tempPath.remove((Object)des);//TODO 这里不强制类型转换的话，集合会默认是按index删除元素
            return;
        }
        tempPath.add(des);
        for(int i=0;i<pre[des].size();i++){
            dfs(pre[des].get(i));
        }
        tempPath.remove((Object)des);
    }

    public static void dijkstra(int start){
        //dijkstra初始化
        for(int i=0;i<MAXN;i++){
            dis[i]=INF;
            pre[i]=new Vector<>();
        }
        dis[start]=0;

        for(int i=0;i<n;i++){
            int u=-1;
            int MIN=INF;
            for(int j=0;j<n;j++){
                if(vis[j]==false&&dis[j]<MIN) {
                    u = j;
                    MIN = dis[j];
                }
            }
            if(u==-1) return;
            vis[u]=true;
            for(int v=0;v<n;v++){
                if(vis[v]==false&&g[u][v]!=INF){
                    if(dis[u]+g[u][v]<dis[v]){
                        dis[v]=dis[u]+g[u][v];
                        pre[v].clear();//一定要记得clear掉
                        pre[v].add(u);
                    }else if(dis[u]+g[u][v]==dis[v]){
                        pre[v].add(u);
                    }
                }
            }
        }
    }
}
