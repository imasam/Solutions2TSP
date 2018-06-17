import java.io.BufferedReader;
import java.io.FileInputStream;  
import java.io.IOException;  
import java.io.InputStreamReader;  
import java.util.Random;  
  
public class Tabu {  
  
    private int MAX_GEN;// ��������  
    private int N;// ÿ�������ھӸ���  
    private int ll;// ���ɳ���  
    private int cityNum; // �������������볤��  
  
    private int[][] distance; // �������  
    private int bestT;// ��ѳ��ִ���  
  
    private int[] Ghh;// ��ʼ·������  

    public int[] getBestTour() {
        return bestTour;
    }

    public int getBestLength() {
        return bestLength;
    }

    private int[] bestTour;// ��õ�·������
    private int bestLength;  
    private int[] LocalGhh;// ������ñ���  
    private int localEvaluation;  
    private int[] tempGhh;// �����ʱ����  
    private int tempEvaluation;  
  
    private int[][] jinji;// ���ɱ�  
  
    private int t;// ��ǰ����  
  
    private Random random;  
  
    public Tabu() {  
  
    }  

    public Tabu(int n, int g, int c, int m) {  
        cityNum = n;  
        MAX_GEN = g;  
        N = c;  
        ll = m;  
    }  

    public void init(String filename) throws IOException {
        // ��ȡ����  
        int[] x;  
        int[] y;  
        String strbuff;  
        BufferedReader data = new BufferedReader(new InputStreamReader(  
                new FileInputStream(filename)));  
        distance = new int[cityNum][cityNum];  
        x = new int[cityNum];  
        y = new int[cityNum];  
        for (int i = 0; i < cityNum; i++) {  
            // // ���ݸ�ʽ:��� x y
            strbuff = data.readLine();
            String[] strcol = strbuff.split(" ");  
            x[i] = Integer.valueOf(strcol[1]);// x
            y[i] = Integer.valueOf(strcol[2]);// y
        }  
        // �������
        for (int i = 0; i < cityNum - 1; i++) {  
            distance[i][i] = 0; // �Խ���Ϊ0  
            for (int j = i + 1; j < cityNum; j++) {  
                double rij = Math  
                        .sqrt(((x[i] - x[j]) * (x[i] - x[j]) + (y[i] - y[j])  
                                * (y[i] - y[j])) / 10.0);
                int tij = (int) Math.round(rij);  
                if (tij < rij) {  
                    distance[i][j] = tij + 1;  
                    distance[j][i] = distance[i][j];  
                } else {  
                    distance[i][j] = tij;  
                    distance[j][i] = distance[i][j];  
                }  
            }  
        }  
        distance[cityNum - 1][cityNum - 1] = 0;  
  
        Ghh = new int[cityNum];  
        bestTour = new int[cityNum];  
        bestLength = Integer.MAX_VALUE;  
        LocalGhh = new int[cityNum];  
        localEvaluation = Integer.MAX_VALUE;  
        tempGhh = new int[cityNum];  
        tempEvaluation = Integer.MAX_VALUE;  
  
        jinji = new int[ll][cityNum];  
        bestT = 0;  
        t = 0;  
  
        random = new Random(System.currentTimeMillis());
    }  
  
    // ��ʼ������Ghh  
    void initGroup() {  
        int i, j;  
        Ghh[0] = random.nextInt(65535) % cityNum;  
        for (i = 1; i < cityNum;)
        {  
            Ghh[i] = random.nextInt(65535) % cityNum;  
            for (j = 0; j < i; j++) {  
                if (Ghh[i] == Ghh[j]) {  
                    break;  
                }  
            }  
            if (j == i) {  
                i++;  
            }  
        }  
    }  
  
    // ���Ʊ����壬���Ʊ���Gha��Ghb  
    public void copyGh(int[] Gha, int[] Ghb) {  
        for (int i = 0; i < cityNum; i++) {  
            Ghb[i] = Gha[i];  
        }  
    }  
  
    public int evaluate(int[] chr) {
        int len = 0;  
        // ���룬��ʼ����,����1,����2...����n  
        for (int i = 1; i < cityNum; i++) {  
            len += distance[chr[i - 1]][chr[i]];  
        }  
        // ����n,��ʼ����  
        len += distance[chr[cityNum - 1]][chr[0]];  
        return len;  
    }  
  
    // ����
    public void Linju(int[] Gh, int[] tempGh) {  
        int i, temp;  
        int ran1, ran2;  
  
        for (i = 0; i < cityNum; i++) {  
            tempGh[i] = Gh[i];  
        }  
        ran1 = random.nextInt(65535) % cityNum;  
        ran2 = random.nextInt(65535) % cityNum;  
        while (ran1 == ran2) {  
            ran2 = random.nextInt(65535) % cityNum;  
        }  
        temp = tempGh[ran1];  
        tempGh[ran1] = tempGh[ran2];  
        tempGh[ran2] = temp;  
    }  
  
    // �жϱ����Ƿ��ڽ��ɱ���  
    public int panduan(int[] tempGh) {  
        int i, j;  
        int flag = 0;  
        for (i = 0; i < ll; i++) {  
            flag = 0;  
            for (j = 0; j < cityNum; j++) {  
                if (tempGh[j] != jinji[i][j]) {  
                    flag = 1;// ����ͬ  
                    break;  
                }  
            }  
            if (flag == 0)// ��ͬ�����ش�����ͬ  
            {
                break;  
            }  
        }  
        if (i == ll)// ����  
        {  
            return 0;// ������  
        } else {  
            return 1;// ����  
        }  
    }  
  
    // �������������  
    public void jiejinji(int[] tempGh) {  
        int i, j, k;  
        // ɾ����һ�����룬���������ǰ
        for (i = 0; i < ll - 1; i++) {  
            for (j = 0; j < cityNum; j++) {  
                jinji[i][j] = jinji[i + 1][j];  
            }  
        }  
  
        // �µı��������ɱ�  
        for (k = 0; k < cityNum; k++) {  
            jinji[ll - 1][k] = tempGh[k];  
        }  
  
    }  
  
    public void solve() {  
        int nn;  
        // ��ʼ������Ghh  
        initGroup();  
        copyGh(Ghh, bestTour);// ���Ƶ�ǰ����Ghh����ñ���bestTour  
        bestLength = evaluate(Ghh);  
  
        while (t < MAX_GEN) {  
            nn = 0;  
            localEvaluation = Integer.MAX_VALUE;  
            while (nn < N) {  
                Linju(Ghh, tempGhh);// �õ���ǰ����Ghh���������tempGhh  
                if (panduan(tempGhh) == 0)// �жϱ����Ƿ��ڽ��ɱ���  
                {  
                    // ����  
                    tempEvaluation = evaluate(tempGhh);  
                    if (tempEvaluation < localEvaluation) {  
                        copyGh(tempGhh, LocalGhh);  
                        localEvaluation = tempEvaluation;  
                    }  
                    nn++;  
                }  
            }  
            if (localEvaluation < bestLength) {  
                bestT = t;  
                copyGh(LocalGhh, bestTour);  
                bestLength = localEvaluation;  
            }  
            copyGh(LocalGhh, Ghh);  
  
            // ����ɱ�LocalGhh������ɱ�  
            jiejinji(LocalGhh);  
            t++;  
        }  
  
        bestTour = Sort(bestTour);
        
        System.out.println("��ѳ��ȳ��ִ�����");  
        System.out.println(bestT);  
        System.out.print("��ѳ���");
        System.out.println(bestLength);  
        System.out.println("���·����");  
        for (int i = 0; i < cityNum; i++) {
            System.out.print(bestTour[i]+"->");
        }
        System.out.print("0");
    }  
    
    public static int[] Sort(int[] args) {
    	int[] args1 = new int[args.length];
    	int flag=0;
    	for(int i=0;i<args.length;i++)
    	{
    		if(args[i]==0) {
    			flag = i;
    			break;
    		}
    	}
    	int k=flag;
    	int p=args.length-k;
    	for(int j=0;k<args.length;j++,k++)
    	{
    		args1[j]=args[k];
    		
    	}
    	
    	for(int j=0;p<args.length;j++,p++)
    	{
    		args1[p]=args[j];
    	}
		return args1;
    }
//
//    public static void main(String[] args) throws IOException {
//        System.out.println("----------------------���������㷨");
//        Tabu tabu = new Tabu(10, 1000, 200, 20);
//        tabu.init("data.txt");
//        tabu.solve();
//    }
}  