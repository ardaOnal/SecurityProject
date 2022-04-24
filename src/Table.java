import java.lang.reflect.Array;
import java.util.ArrayList;

public class Table extends ArrayList<Record>
{
    private ArrayList<Record> records;

    public Table()
    {
        records = new ArrayList<Record>();
    }

    public Table(ArrayList records) {this. records = records;}

    public ArrayList<Record> getRecords(){
        return records;
    }

    public void setRecords( ArrayList<Record> records){
        this.records = records;
    }
}
