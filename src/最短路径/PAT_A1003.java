package 最短路径;

import java.util.Scanner;

/**
 * 有个问题？
 * 邻接表怎么记录边权?
 * 把边权记录到节点里，节点有两个属性，一个是编号，另一个是边权
 */

/**
 * 保证最短距离的同时获取最大点权
 * 1.建图：无向图，带环，每个节点有权重，有边权--->邻接矩阵,初始化边权为INF
 * 2.常规dijkstra，更新最短路径时更新最大点权--->需要开辟一个数组记录点权，另一个数组记录起点s到每个点的最大点权和
 * 3.点的序号从0开始到n-1
 */
public class PAT_A1003 {

    final static int MAXN=501;
    final static int INF=100000001;
    static int n;//节点个数
    static int m;//边数
    static int c1;//起点
    static int c2;//终点
    static int[] vertexWeight=new int[MAXN];//记录点权
    static int[][] g=new int[MAXN][MAXN];//记录边权

    //Dijkstra:
    static boolean[] vis=new boolean[MAXN];
    static int[] d=new int[MAXN];
    static int[] sumVerWeight=new int[MAXN];//记录最大点权和 //记得初始化
    static int[] shortPaths=new int[MAXN];//记录最短路径条数 //记得初始化

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        n= scanner.nextInt();
        m= scanner.nextInt();
        c1= scanner.nextInt();
        c2= scanner.nextInt();
        for(int i=0;i<n;i++){
            vertexWeight[i]= scanner.nextInt();
        }
        for(int i=0;i<MAXN;i++){
            for(int j=0;j<MAXN;j++){
                g[i][j]=INF;
            }
        }
        for (int i=0;i<m;i++){
            int v1= scanner.nextInt();
            int v2= scanner.nextInt();
            int length= scanner.nextInt();
            g[v1][v2]=length;
            g[v2][v1]=length;//TODO 他妈的无向图这里不要忘了
        }

        //以上建图完毕
        /**
         * 则通过Dijkstra求出最短路径个数、求出最短路径时的最大点权
         */
        dijkstra(c1);
        System.out.print(shortPaths[c2]+" ");
        System.out.print(sumVerWeight[c2]);
    }

    public static void dijkstra(int start){
        //Dijkstra初始化操作
        for(int i=0;i<MAXN;i++){
            d[i]=INF;
        }
        d[start]=0;
        for(int i=0;i<MAXN;i++){
            sumVerWeight[i]=0;
            shortPaths[i]=0;
        }
        sumVerWeight[start]=vertexWeight[start];
        shortPaths[start]=1;

        //初始化完成
        for (int i=0;i<n;i++){
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
                        shortPaths[v]=shortPaths[u];//TODO 记得更新最短路径条数
                        sumVerWeight[v]=sumVerWeight[u]+vertexWeight[v];//TODO 记得更新点权和
                    }else if((d[u]+g[u][v]==d[v])){//TODO 当d[u]+g[u][v]==d[v]时，无论是否sumVerWeight[u]+vertexWeight[v]>sumVerWeight[v],都应当让shortPaths[v]+=shortPaths[u]，因为最短路径条数
                                                       //TODO     的依据仅仅是第一标尺的距离，与点权无关
                        shortPaths[v]+=shortPaths[u];
                        if(sumVerWeight[u]+vertexWeight[v]>sumVerWeight[v]){
                            sumVerWeight[v]=sumVerWeight[u]+vertexWeight[v];
                        }
                    }
                }
            }
        }
    }
}
