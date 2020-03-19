public class KNN {
    //rank值
    int nNeighbours;
    //每个样本对于识别体的距离和对应的标签
    KnnNode dist[];
    //计数器
    int counter;

    public KNN(int n) {
        this.nNeighbours=n;
        dist=new KnnNode[n];
        counter=n;
    }


    int index = 0;
    public void sort(int sample[][],int pixel[][],int arrayLength,String firstCharacter) {
        KnnNode temp=new KnnNode(distance(sample,pixel,arrayLength),firstCharacter);
        //首先，把k个槽先填满；
        if(counter>0) {
            dist[index++]=temp;
            counter--;
        }
        //找出rank值中距离样本最远的样本替换
        else{
            int flag=0;
            double max=0;
            for(int i=0;i<this.nNeighbours;i++) {
                if(dist[i].distance>max) {
                    max=dist[i].distance;
                    flag=i;
                }
            }
            if(max>temp.distance)
                dist[flag]=temp;
        }
//        System.out.println(distance(sample,pixel,arrayLength));
    }


    //算欧式距离
    public double distance(int sample[][],int pixel[][],int arrayLength) {
        int sum=0;
        for(int i=0;i<arrayLength;i++) {
            for(int j=0;j<arrayLength;j++) {
                int s1=sample[i][j];
                int s2=pixel[i][j];
                sum+=(s1-s2)*(s1-s2);
            }
        }
        return Math.sqrt(sum);
    }


    public String predict() {
        double[]distanceArray = new double [nNeighbours];
        String []firstCharacterArray = new String [nNeighbours];
        double min = dist[0].distance;
        int temp = 0;
        for(int i = 0;i<nNeighbours;i++)
        {
            distanceArray[i] = dist[i].distance;
            firstCharacterArray[i] = dist[i].firstCharacter;
        }
        for(int i = 0;i<nNeighbours;i++)
        {
            if(min>dist[i].distance)
            {
                min = dist[i].distance;
                temp = i;
            }
        }

        System.out.print(dist[temp].distance+" "+dist[temp].firstCharacter+" ");
        return dist[temp].firstCharacter;
    }
}
class KnnNode {
    //这个是它与样本的距离
    public	double distance;
    //这个是保存文件的第一个字母
    public	String firstCharacter;
    public KnnNode(double distance,String firstCharacter) {
        this.distance=distance;
        this.firstCharacter=firstCharacter;
    }
}