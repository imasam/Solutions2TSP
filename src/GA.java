import java.io.BufferedReader;
import java.io.FileInputStream;  
import java.io.IOException;  
import java.io.InputStreamReader;  
import java.util.Random;  

public class GA {
	private int scale;        // ��Ⱥ��ģ
    private int cityNum;      // ����������Ⱦɫ�峤��
    private int MAX_GEN;      // ���д���
    private int[][] distance; // �������  
    private int bestT;        // ��ѳ��ִ���
    private int bestLength;   // ��ѳ���
    private int[] bestTour;   // ���·��

    private int[][] oldPopulation;  
    private int[][] newPopulation;  // �µ���Ⱥ���Ӵ���Ⱥ
    private int[] fitness;          // ��Ⱥ��Ӧ�ȣ���ʾ��Ⱥ�и����������Ӧ��
  
    private float[] Pi; // ��Ⱥ�и���������ۼƸ���
    private float Pc;   // �������
    private float Pm;   // �������
    private int t;      // ��ǰ����
  
    private Random random;

    public GA(int s, int n, int g, float c, float m) {  
        scale = s;  
        cityNum = n;  
        MAX_GEN = g;  
        Pc = c;  
        Pm = m;  
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
            // ��ȡһ�����ݣ����ݸ�ʽ1 6734 1453  
            strbuff = data.readLine();  
            // �ַ��ָ�  
            String[] strcol = strbuff.split(" ");  
            x[i] = Integer.valueOf(strcol[1]);// x����  
            y[i] = Integer.valueOf(strcol[2]);// y����  
        }  
        // ����������  
        // ����Ծ������⣬������㷽��Ҳ��һ�����˴��õ���att48��Ϊ����������48�����У�������㷽��Ϊαŷ�Ͼ��룬����ֵΪ10628  
        for (int i = 0; i < cityNum - 1; i++) {  
            distance[i][i] = 0; // �Խ���Ϊ0  
            for (int j = i + 1; j < cityNum; j++) {  
                double rij = Math  
                        .sqrt(((x[i] - x[j]) * (x[i] - x[j]) + (y[i] - y[j])  
                                * (y[i] - y[j])) / 10.0);  
                // �������룬ȡ��  
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
  
        bestLength = Integer.MAX_VALUE;  
        bestTour = new int[cityNum + 1];  
        bestT = 0;  
        t = 0;  
  
        newPopulation = new int[scale][cityNum];  
        oldPopulation = new int[scale][cityNum];  
        fitness = new int[scale];  
        Pi = new float[scale];  
  
        random = new Random(System.currentTimeMillis());
    }  
  
    // ��ʼ����Ⱥ  
    void initGroup() {  
        int i, j, k;  
        // Random random = new Random(System.currentTimeMillis());  
        for (k = 0; k < scale; k++)// ��Ⱥ��  
        {  
            oldPopulation[k][0] = 0;  
            for (i = 1; i < cityNum;)// Ⱦɫ�峤��  
            {  
                oldPopulation[k][i] = random.nextInt(65535) % cityNum;  
                for (j = 0; j < i; j++) {  
                    if (oldPopulation[k][i] == oldPopulation[k][j]) {  
                        break;  
                    }  
                }  
                if (j == i) {  
                    i++;  
                }  
            }  
        }  
  
        /* 
         * for(i=0;i<scale;i++) { for(j=0;j<cityNum;j++) { 
         * System.out.print(oldPopulation[i][j]+","); } System.out.println(); } 
         */  
    }  
  
    public int evaluate(int[] chromosome) {  
        // 0123  
        int len = 0;  
        // Ⱦɫ�壬��ʼ����,����1,����2...����n  
        for (int i = 1; i < cityNum; i++) {  
            len += distance[chromosome[i - 1]][chromosome[i]];  
        }  
        // ����n,��ʼ����  
        len += distance[chromosome[cityNum - 1]][chromosome[0]];  
        return len;  
    }  
  
    //������Ӧ��fitness[max]�����ڶ���ѡ�����Pi[max]
    void countRate() {  
        int k;  
        double sumFitness = 0;// ��Ӧ���ܺ�  
  
        double[] tempf = new double[scale];  
  
        for (k = 0; k < scale; k++) {  
            tempf[k] = 10.0 / fitness[k];  
            sumFitness += tempf[k];  
        }  
  
        Pi[0] = (float) (tempf[0] / sumFitness);  
        for (k = 1; k < scale; k++) {  
            Pi[k] = (float) (tempf[k] / sumFitness + Pi[k - 1]);  
        }

        // ��ʾ
        for(k=0;k<scale;k++) { System.out.println(fitness[k]+" "+Pi[k]); }

    }  
  
    // ��ѡĳ����Ⱥ����Ӧ����ߵĸ��壬ֱ�Ӹ��Ƶ��Ӵ���
    public void selectBestGh() {  
        int k, i, maxid;  
        int maxevaluation;  
  
        maxid = 0;  
        maxevaluation = fitness[0];  
        for (k = 1; k < scale; k++) {  
            if (maxevaluation > fitness[k]) {  
                maxevaluation = fitness[k];  
                maxid = k;  
            }  
        }  
  
        if (bestLength > maxevaluation) {  
            bestLength = maxevaluation;  
            bestT = t;// ��õ�Ⱦɫ����ֵĴ���;  
            for (i = 0; i < cityNum; i++) {  
                bestTour[i] = oldPopulation[maxid][i];  
            }  
        }
        copyGh(0, maxid);// ��������Ⱥ����Ӧ����ߵ�Ⱦɫ��k���Ƶ�����Ⱥ�У����ڵ�һλ0  
    }  
  
    // ����Ⱦɫ�壬k��ʾ��Ⱦɫ������Ⱥ�е�λ�ã�kk��ʾ�ɵ�Ⱦɫ������Ⱥ�е�λ��  
    public void copyGh(int k, int kk) {  
        int i;  
        for (i = 0; i < cityNum; i++) {  
            newPopulation[k][i] = oldPopulation[kk][i];  
        }  
    }  
  
    // ����ѡ�������ѡ  
    public void select() {  
        int k, i, selectId;  
        float ran1;
        for (k = 1; k < scale; k++) {  
            ran1 = (float) (random.nextInt(65535) % 1000 / 1000.0);
            // ������ʽ  
            for (i = 0; i < scale; i++) {  
                if (ran1 <= Pi[i]) {  
                    break;  
                }  
            }  
            selectId = i;
            copyGh(k, selectId);  
        }  
    }
  
    //����
    public void evolution() {  
        int k;  
        // ��ѡĳ����Ⱥ����Ӧ����ߵĸ���  
        selectBestGh();  
  
        // ����ѡ�������ѡscale-1����һ������  
        select();  

        float r;  
  
        for (k = 1; k + 1 < scale / 2; k = k + 2) {  
            r = random.nextFloat();// /��������  
            if (r < Pc) {  
                OXCross(k, k + 1);// ���н���
            } else {  
                r = random.nextFloat();// /��������  
                // ����  
                if (r < Pm) {  
                    OnCVariation(k);  
                }  
                r = random.nextFloat();// /��������  
                // ����  
                if (r < Pm) {  
                    OnCVariation(k + 1);  
                }  
            }  
        }  
        if (k == scale / 2 - 1)// ʣ���һ��Ⱦɫ��û�н���L-1  
        {  
            r = random.nextFloat();// /��������  
            if (r < Pm) {  
                OnCVariation(k);  
            }  
        }  
  
    }
  
    // ����
    public void OXCross(int k1, int k2) {  
        int i, j, k, flag;  
        int ran1, ran2, temp;  
        int[] Gh1 = new int[cityNum];  
        int[] Gh2 = new int[cityNum];  
  
        ran1 = random.nextInt(65535) % (cityNum-1)+1;  
        ran2 = random.nextInt(65535) % (cityNum-1)+1;  
        while (ran1 == ran2) {  
            ran2 = random.nextInt(65535) % (cityNum-1)+1;  
        }  
  
        if (ran1 > ran2)// ȷ��ran1<ran2  
        {  
            temp = ran1;  
            ran1 = ran2;  
            ran2 = temp;  
        }  
  
        Gh1[0]=0;
        Gh2[0]=0;
        // ��Ⱦɫ��1�еĵ��������Ƶ�Ⱦɫ��2���ײ�  
        for (i = 1, j = ran2; j < cityNum; i++, j++) {  
            Gh2[i] = newPopulation[k1][j];  
        }  
  
        flag = i;// Ⱦɫ��2ԭ����ʼλ��  
  
        for (k = 1, j = flag; j < cityNum;)// Ⱦɫ�峤��  
        {  
            Gh2[j] = newPopulation[k2][k++];  
            for (i = 1; i < flag; i++) {  
                if (Gh2[i] == Gh2[j]) {  
                    break;  
                }  
            }  
            if (i == flag) {  
                j++;  
            }  
        }   
  
        flag = ran1;  
        for (k = 1, j = 1; k < cityNum;)// Ⱦɫ�峤��  
        {  
            Gh1[j] = newPopulation[k1][k++];  
            for (i = 1; i < flag; i++) {  
                if (newPopulation[k2][i] == Gh1[j]) {  
                    break;  
                }  
            }  
            if (i == flag) {  
                j++;  
            }  
        }  
  
        flag = cityNum - ran1 +1;  
  
        for (i = 1, j = flag; j < cityNum; j++, i++) {  
            Gh1[j] = newPopulation[k2][i];  
        }  
  
        for (i = 0; i < cityNum; i++) {  
            newPopulation[k1][i] = Gh1[i];// �Ż���Ⱥ  
            newPopulation[k2][i] = Gh2[i];// �Ż���Ⱥ  
        }  
    }  
  
    // ��ζԻ���������  
    public void OnCVariation(int k) {  
        int ran1, ran2, temp;  
        int count;// �Ի�����  
  
        count = random.nextInt(65535) % cityNum;  
  
        for (int i = 0; i < count; i++) {  
  
            ran1 = random.nextInt(65535) % (cityNum-1)+1;  
            ran2 = random.nextInt(65535) % (cityNum-1)+1;  
            while (ran1 == ran2) {  
                ran2 = random.nextInt(65535) % (cityNum-1)+1;  
            }  
            temp = newPopulation[k][ran1];  
            newPopulation[k][ran1] = newPopulation[k][ran2];  
            newPopulation[k][ran2] = temp;  
        }
    }  
  
    public void solve() {  
        int i;  
        int k;  
  
        // ��ʼ����Ⱥ  
        initGroup();  
        // �����ʼ����Ⱥ��Ӧ�ȣ�Fitness[max]  
        for (k = 0; k < scale; k++) {  
            fitness[k] = evaluate(oldPopulation[k]);  
            // System.out.println(fitness[k]);  
        }  
        // �����ʼ����Ⱥ�и���������ۻ����ʣ�Pi[max]  
        countRate();  
        System.out.println("��ʼ��Ⱥ...");  
        for (k = 0; k < scale; k++) {  
            for (i = 0; i < cityNum; i++) {  
                System.out.print(oldPopulation[k][i] + ",");  
            }  
            System.out.println();  
            System.out.println("----" + fitness[k] + " " + Pi[k]);  
        }  
          
        for (t = 0; t < MAX_GEN; t++) {  
            evolution();  
            //evolution();  
            // ������ȺnewGroup���Ƶ�����ȺoldGroup�У�׼����һ������  
            for (k = 0; k < scale; k++) {  
                for (i = 0; i < cityNum; i++) {  
                    oldPopulation[k][i] = newPopulation[k][i];  
                }  
            }  
            // ������Ⱥ��Ӧ��  
            for (k = 0; k < scale; k++) {  
                fitness[k] = evaluate(oldPopulation[k]);  
            }  
            // ������Ⱥ�и���������ۻ�����  
            countRate();  
        }  
  
        System.out.println("�����Ⱥ...");  
        for (k = 0; k < scale; k++) {  
            for (i = 0; i < cityNum; i++) {  
                System.out.print(oldPopulation[k][i] + ",");  
            }  
            System.out.println();  
            System.out.println("---" + fitness[k] + " " + Pi[k]);  
        }  
  
        System.out.println("��ѳ��ȳ��ִ�����");  
        System.out.println(bestT);  
        System.out.print("��ѳ���");
        System.out.println(bestLength);  
        System.out.println("���·����");  
        for (i = 0; i < cityNum; i++) {
            System.out.print(bestTour[i]+"->");
        }
        System.out.print("0");
  
    }  
  
      
    public int getBestLength() {
		return bestLength;
	}

	public int[] getBestTour() {
		return bestTour;
	}


//    public static void main(String[] args) throws IOException {
//        System.out.println("----------------------�Ŵ��㷨");
//        GA ga = new GA(6, 10, 1000, 0.8f, 0.9f);
//        ga.init("data.txt");
//        ga.solve();
//    }
}