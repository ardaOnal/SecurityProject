import java.lang.reflect.Array;
import java.util.ArrayList;

/*
    Table class holding an ArrayList which holds Record objects,
    which corresponds to each row in the GUI
 */
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
