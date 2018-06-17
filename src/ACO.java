import java.io.BufferedReader;
import java.io.FileInputStream;  
import java.io.IOException;  
import java.io.InputStreamReader;  

public class ACO {  
	  
    private Ant[] ants;         // ����
    private int antNum;         // ��������
    private int cityNum;        // ��������
    private int MAX_GEN;        // ���д���
    private float[][] pheromone;// ��Ϣ�ؾ���
    private int[][] distance;   // �������
    private int bestLength;     // ��ѳ���
    private int[] bestTour;     // ���·��
  
    // ����
    private float alpha;  
    private float beta;  
    private float rho;

    public ACO(int n, int m, int g, float a, float b, float r) {  
        cityNum = n;  
        antNum = m;  
        ants = new Ant[antNum];  
        MAX_GEN = g;  
        alpha = a;  
        beta = b;  
        rho = r;  
    }

    public int getBestLength() {
        return bestLength;
    }

    public int[] getBestTour() {
        return bestTour;
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
            // ���ݸ�ʽ:��� x y
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
        // ��ʼ����Ϣ�ؾ���  
        pheromone = new float[cityNum][cityNum];  
        for (int i = 0; i < cityNum; i++) {  
            for (int j = 0; j < cityNum; j++) {  
                pheromone[i][j] = 0.1f;
            }  
        }  
        bestLength = Integer.MAX_VALUE;  
        bestTour = new int[cityNum];  
        // �����������  
        for (int i = 0; i < antNum; i++) {  
            ants[i] = new Ant(cityNum);  
            ants[i].init(distance, alpha, beta);  
        }  
    }  
  
    public void solve() {
        for (int g = 0; g < MAX_GEN; g++) {
            for (int i = 0; i < antNum; i++) {
                for (int j = 1; j < cityNum; j++) {  
                    ants[i].selectNextCity(pheromone);  
                }
                // ���ɱ���ʼ����,����1,����2...����n,��ʼ����
                ants[i].getTabu().add(ants[i].getFirstCity());  
                // �Ƚ���Ѿ���
                if (ants[i].getTourLength() < bestLength) {
                    bestLength = ants[i].getTourLength();  
                    for (int k = 0; k < cityNum; k++) {  
                        bestTour[k] = ants[i].getTabu().get(k).intValue();  
                    }  
                }  
                // ������Ϣ�ؾ���
                for (int j = 0; j < cityNum; j++) {  
                    ants[i].getDelta()[ants[i].getTabu().get(j).intValue()][ants[i]  
                            .getTabu().get(j + 1).intValue()] = (float) (1. / ants[i]  
                            .getTourLength());  
                    ants[i].getDelta()[ants[i].getTabu().get(j + 1).intValue()][ants[i]  
                            .getTabu().get(j).intValue()] = (float) (1. / ants[i]  
                            .getTourLength());  
                }  
            }  
            // ������Ϣ��  
            updatePheromone();  
            // ���³�ʼ������  
            for (int i = 0; i < antNum; i++) {  
                ants[i].init(distance, alpha, beta);  
            }  
        }  

        printOptimal();  
    }  
  
    // ������Ϣ��  
    private void updatePheromone() {  
        // ��Ϣ�ػӷ�  
        for (int i = 0; i < cityNum; i++)  
            for (int j = 0; j < cityNum; j++)  
                pheromone[i][j] = pheromone[i][j] * (1 - rho);  
        // ��Ϣ�ظ���  
        for (int i = 0; i < cityNum; i++) {  
            for (int j = 0; j < cityNum; j++) {  
                for (int k = 0; k < antNum; k++) {  
                    pheromone[i][j] += ants[k].getDelta()[i][j];  
                }  
            }  
        }  
    }  
  
    private void printOptimal() {  
    	bestTour = Sort(bestTour);
        System.out.println("��ѳ��ȣ�" + bestLength);
        System.out.print("���·��: ");
        for (int i = 0; i < cityNum ; i++) {  
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
//        System.out.println("----------------------��Ⱥ�㷨");
//        ACO aco = new ACO(10, 10, 720, 1.f, 5.f, 0.5f);
//        aco.init("data.txt");
//        aco.solve();
//    }
}  