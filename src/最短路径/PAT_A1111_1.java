package 最短路径;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;

public class PAT_A1111_1 {

    final static int MAXN=510;
    final static int INF=10000001;
    static int n;
    static int m;
    static int[][] gLength=new int[MAXN][MAXN];
    static int[][] gTime=new int[MAXN][MAXN];
    static int start;
    static int des;
    static int[] lengthPre=new int[MAXN];
    static int[] timePre=new int[MAXN];
    static Vector<Integer> lengthPath=new Vector<>();
    static Vector<Integer> timePath=new Vector<>();
    static int[] lengthTime=new int[MAXN];
    static int[] timeVertexNum=new int[MAXN];
    static int Distance;
    static int Time;
    //dijkstra:
    static int[] d=new int[MAXN];
    static boolean[] vis=new boolean[MAXN];

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line = br.readLine();
        String[] s = line.split(" ");
        n= Integer.parseInt(s[0]);
        m= Integer.parseInt(s[1]);
        for (int i=0;i<MAXN;i++){
            for(int j=0;j<MAXN;j++){
                gLength[i][j]=INF;
                gTime[i][j]=INF;
            }
        }
        for(int i=0;i<m;i++){
            String line1 = br.readLine();
            String[] s1 = line1.split(" ");
            int v1= Integer.parseInt(s1[0]);
            int v2= Integer.parseInt(s1[1]);
            int flag= Integer.parseInt(s1[2]);
            int length= Integer.parseInt(s1[3]);
            int time= Integer.parseInt(s1[4]);
            gLength[v1][v2]=length;
            gTime[v1][v2]=time;
            if(flag!=1){//双向
                gLength[v2][v1]=length;
                gTime[v2][v1]=time;
            }
        }
        String line1 = br.readLine();
        String[] s1 = line1.split(" ");
        start= Integer.parseInt(s1[0]);
        des= Integer.parseInt(s1[1]);
        br.close();
        //dijkstra得出最短距离路径(双重标尺)
        dijkstraForLength(start);
        Distance=d[des];
        //dijkstra得出最短时间路径(双重标尺)
        dijkstraForTime(start);
        Time=d[des];
        dfsForLength(des);
        dfsForTime(des);

        boolean flag=true;
        if(lengthPath.size()!=timePath.size()){
            flag=false;
        }else{
            for(int i=0;i<lengthPath.size();i++){
                if(lengthPath.get(i)!=timePath.get(i)){
                    flag=false;
                    break;
                }
            }
        }

        if(flag){
            //Distance = 3; Time = 4: 3 -> 2 -> 5
            System.out.print("Distance = "+Distance+"; ");
            System.out.print("Time = "+Time+": ");
            for(int i=lengthPath.size()-1;i>=0;i--){
                if(i==0){
                    System.out.print(lengthPath.get(i));
                }else{
                    System.out.print(lengthPath.get(i)+" -> ");
                }
            }
        }else{
//            Distance = 6: 3 -> 4 -> 8 -> 5
//            Time = 3: 3 -> 1 -> 5
            System.out.print("Distance = "+Distance+": ");
            for(int i=lengthPath.size()-1;i>=0;i--){
                if(i==0){
                    System.out.println(lengthPath.get(i));
                }else{
                    System.out.print(lengthPath.get(i)+" -> ");
                }
            }
            System.out.print("Time = "+Time+": ");
            for(int i=timePath.size()-1;i>=0;i--){
                if(i==0){
                    System.out.println(timePath.get(i));
                }else{
                    System.out.print(timePath.get(i)+" -> ");
                }
            }
        }
    }

    public static void dfsForLength(int v){//记录倒序路径
        lengthPath.add(v);
        if(v==start) return;
        dfsForLength(lengthPre[v]);
    }

    public static void dfsForTime(int v){
        timePath.add(v);
        if(v==start) return;
        dfsForTime(timePre[v]);
    }

    public static void dijkstraForLength(int begin){
        for(int i=0;i<MAXN;i++){
            d[i]=INF;
            vis[i]=false;
            lengthTime[i]=0;
            lengthPre[i]=i;
        }
        d[begin]=0;
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
                if(vis[v]==false&&gLength[u][v]!=INF){
                    if(d[u]+gLength[u][v]<d[v]){
                        d[v]=d[u]+gLength[u][v];
                        lengthPre[v]=u;
                        lengthTime[v]=lengthTime[u]+gTime[u][v];//TODO 不要忘记更新
                    }else if(d[u]+gLength[u][v]==d[v]){
                        if(lengthTime[v]>lengthTime[u]+gTime[u][v]){
                            lengthTime[v]=lengthTime[u]+gTime[u][v];
                            lengthPre[v]=u;
                        }
                    }
                }
            }
        }
    }

    public static void dijkstraForTime(int begin){
        for(int i=0;i<MAXN;i++){
            d[i]=INF;
            vis[i]=false;
            timeVertexNum[i]=0;
            timePre[i]=i;
        }
        d[begin]=0;
        timeVertexNum[begin]=1;
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
                if(vis[v]==false&&gTime[u][v]!=INF){
                    if(d[u]+gTime[u][v]<d[v]){
                        d[v]=d[u]+gTime[u][v];
                        timePre[v]=u;
                        timeVertexNum[v]=timeVertexNum[u]+1;//TODO 不要忘记更新
                    }else if(d[u]+gTime[u][v]==d[v]){
                        if(timeVertexNum[v]>timeVertexNum[u]+1){
                            timeVertexNum[v]=timeVertexNum[u]+1;
                            timePre[v]=u;
                        }
                    }
                }
            }
        }
    }

}
