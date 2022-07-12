package 最短路径;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;

/**
 * 题目分析:
 * 1.PBMC为一个节点，标号为0，为起点
 * 2.其余节点标号从1到n
 * 3.题目给定一个问题节点标号Sp
 * 4.二重标尺: 最短距离 涉及点权(从PBMC派出最少的自行车使得路径上每个节点的权重都为最大权重的一半)
 * //TODO 其实仔细读题，最后结果是依赖三重标尺的:1.最短距离 2.最小need 3.最短距离与最小need相同的路径有多条时，取决于最小back
 *
 * 思路：
 * 1.邻接矩阵建图，记录点权
 * 2.dijkstra记录下所有最短路径
 * 3.dfs这些最短路径，获取optNeed
 */
public class PAT_A1018 {

    final static int INF=10000001;
    final static int MAXN=510;
    static int CMAX;//最大权重
    static int halfCMAX;//最大权重一半
    static int N;//节点个数
    static int SP;//终点的序号
    static int M;//总边数
    static int[][] g=new int[MAXN][MAXN];
    static int[] verWeight=new int[MAXN];//点权

    //dijkstra:
    static int[] d=new int[MAXN];
    static boolean[] vis=new boolean[MAXN];
    static Vector<Integer>[] pres=new Vector[MAXN];

    //dfs:
    static Vector<Integer> optPath=new Vector<>();
    static Vector<Integer> tempPath=new Vector<>();
    static int optNeed=INF;//记录要从起点运出多少车
    static int optBack=INF;//记录要运多少车回起点

    public static void main(String[] args) throws IOException {
        //输入
        InputStreamReader ir = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(ir);
        String line = br.readLine();
        String[] s = line.split(" ");
        CMAX=Integer.parseInt(s[0]);
        halfCMAX=CMAX/2;
        N=Integer.parseInt(s[1]);
        SP=Integer.parseInt(s[2]);
        M=Integer.parseInt(s[3]);
        String line1 = br.readLine();
        String[] s1 = line1.split(" ");
        for(int i=0;i<N;i++){
            verWeight[i+1]=Integer.parseInt(s1[i])-halfCMAX;//节点序号从1开始 //TODO 我们保存的权重不是题目直接给的权重，而是处理后的权重--->对后续求第二标尺有帮助
                                                                            //TODO 如果处理后的权重大于0，说明该节点的车可往后运(back)；小于0则说明该节点需要前面节点(包括起点)的车(need)
        }
        for(int i=0;i<MAXN;i++){
            for(int j=0;j<MAXN;j++){
                g[i][j]=INF;
            }
        }
        for(int i=0;i<M;i++){
            String line2 = br.readLine();
            String[] s2 = line2.split(" ");
            int v1=Integer.parseInt(s2[0]);
            int v2=Integer.parseInt(s2[1]);
            int dis=Integer.parseInt(s2[2]);
            g[v1][v2]=dis;
            g[v2][v1]=dis;
        }
        //输入完毕

        //dijkstra记录下最短路径:
        dijkstra(0);
        //dfs来求得最优路径以及最优解
        dfs(SP);
        System.out.print(optNeed+" ");
        for(int i=optPath.size()-1;i>=0;i--){
            if(i==0){
                System.out.print(optPath.get(0)+" ");
            }else{
                System.out.print(optPath.get(i)+"->");
            }
        }
        System.out.print(optBack);
    }

    public static void dfs(int v){
        if(v==0){
            tempPath.add(v);
            int tempNeed=0;
            int tempBack=0;
            //TODO 逆序遍历tempPath求出tempNeed与tempBack并更新optNeed与optBack
            //TODO 求tempNeed与tempBack的方法就是模拟＋动态更新
            for(int i=tempPath.size()-1;i>=0;i--){//逆序访问tempPath实际上是正序访问路径
                int id=tempPath.get(i);
                if(verWeight[id]>0){
                    tempBack+=verWeight[id];
                }else{
                    if(tempBack > (0-verWeight[id])){
                        tempBack+=verWeight[id];
                    }else{
                        tempNeed+=0-(tempBack+verWeight[id]);
                        tempBack=0;
                    }
                }
            }
            if(tempNeed<optNeed){
                optNeed=tempNeed;
                optBack=tempBack;
                optPath.clear();
                for (Integer integer : tempPath) {
                    optPath.add(integer);
                }
            }else if(tempNeed==optNeed&&tempBack<optBack){//TODO 此时满足：最短路径+最小need，题目说如果有多种这样情况时，再根据最小back判优,保证答案的唯一性
                optBack=tempBack;
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

    public static void falseDfs(int des){//TODO 最开始写的错误的dfs，主要是最优解求错了
        if(des==0){
            tempPath.add(des);
            int tempValue=0;
            for(int i=0;i<tempPath.size();i++){//TODO 这里对最优解的求解方式是错误的，因为后面站点的车不能往前面运 从95行到118行
                                                //TODO 属于想都没想清楚就偷懒认为理所当然
                tempValue+=verWeight[tempPath.get(i)];
            }
            tempValue=halfCMAX*(tempPath.size()-1)-tempValue;
            if(tempValue>0){//没有车需要运回起点
                optBack=0;
                if(tempValue<optNeed){
                    optNeed=tempValue;
                    optPath.clear();
                    for (Integer integer : tempPath) {
                        optPath.add(integer);
                    }
                }
            }else{//没有车需要从起点运出
                tempValue=-tempValue;
                optNeed=0;
                if(tempValue<optBack){
                    optBack=tempValue;
                    optPath.clear();
                    for (Integer integer : tempPath) {
                        optPath.add(integer);
                    }
                }
            }
            tempPath.remove((Object)des);
            return;//TODO 不要忘记了，否则会导致StackOverflowError
        }
        tempPath.add(des);
        for(int i=0;i<pres[des].size();i++){
            dfs(pres[des].get(i));
        }
        tempPath.remove((Object)des);
    }

    public static void dijkstra(int begin){
        //初始化
        for(int i=0;i<MAXN;i++){
            d[i]=INF;
            pres[i]=new Vector<>();
        }
        d[begin]=0;

        for(int i=0;i<N+1;i++){//有N+1个节点
            int u=-1;
            int MIN=INF;
            for(int j=0;j<N+1;j++){
                if(vis[j]==false&&d[j]<MIN){
                    u=j;
                    MIN=d[j];
                }
            }
            if(u==-1) return;
            vis[u]=true;
            for(int v=0;v<N+1;v++){
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
