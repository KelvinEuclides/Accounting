package com.anje.kelvin.accontingapp.BaseDeDados;

        import android.content.ContentValues;
        import android.content.Context;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;
        import android.util.Log;

        import java.io.FileOutputStream;
        import java.io.IOException;
        import java.io.InputStream;
        import java.io.OutputStream;
        import java.text.SimpleDateFormat;
        import java.util.Calendar;
        import java.util.Date;

/**
 * Created by sala on 02-02-2018.
 */

public class DBhelper extends SQLiteOpenHelper {
    private static String DB_NAME = "Aconting_DB.sqlite";
    private static String DB_PATH = "";
    private SQLiteDatabase database;
    private final Context myContext;
    // Nome De Tabelas
    private static final String TABLE_USER = "Usuario";
    private static final String TABLE_CONTA="Conta";
    private static final String TABLE_DESPESA="Despesa_Realm";
    private static final String TABLE_DEPOSITO="Deposito";
    private static final String TABLE_TRANSACAO="Transacao_db";
    private static final String TABLE_STOCK="StocK";
    private static final String TABLE_ITEM="Item";
    private static final String TABLE_VENDA="ReDespesa";
    // Tabela Usuario
    private static final String Nome_usuario= "nome_usuario";
    private static final String Id_usuario = "id_usuario";
    private static final String Pin="pin_usuario";
    private static final String Telefone_usuario= "telefone";
    //Tabela Conta
    private static final String Id_conta= "id_conta";
    private static final String Id_conta_usuario = "id_usuario_usuario";
    private static final String Nome_conta = "nome_conta";
    private static final String Saldo_conta= "saldo_Conta";
    //Table Stock
    private static final String Id_stock="id_stock";
    private static final String Id_conta_stock="id_conta_stock";
    //Table Item
    private static final String Id_itens="id_itens";
    private static final String Id_stock_item="id_stock";
    private static final String Nome_item="id_itens";
    private static final String Quantidade="quantidade";
    private static final String Quantidade_disp="quantidade_disp";
    private static final String preco_item="preco";
    private static final String data_item="data";
    //Table ReDespesa
    private static final String Id_venda="id_venda";
    private static final String Id_stock_venda="id_stock";
    private static final String Id_item_venda="id_venda";
    private static final String descricao_venda="descricao";
    private static final String data_venda="data";
    private static final String valor_venda="valor";
    //Table Transacao_db
    private static final String Id_transacao="id_transacao";
    private  static final String Id_conta_transacao="id_conta_transacao";
    //Table Deposito
    private  static final String Id_deposito="id_deposito";
    private static final String Recorrencia="Recorrencia";
    private  static final String Id_transicao_deposito="id_transicao_deposito";
    private  static final String valor_deposito="valor_deposito";
    private static final String data_deposito="data";
    private static final String discricao="discricao";
    //Table Despesa_Realm
    private  static final String Id_despesa="id_despesa";
    private static final String Recorrencia_despesa="recorrencia";
    private  static final String Id_transicao_despesa="id_transicao_despesa";
    private  static final String valor_despesa="valor_despesa";
    private static final String data_despesa="data";
    private static final String discricao_despesa="discricao";




    public DBhelper(Context context) {
        super(context, DB_NAME, null, 11);

        this.myContext = context;

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_USER_TABLE ="CREATE TABLE "+TABLE_USER+"(" +
                Id_usuario+" INT NOT NULL AUTO_INCREMENT," +Telefone_usuario+
                "  INT NOT NULL," +
                Pin+"  INT NOT NULL," +Nome_usuario+
                "VARCHAR(45) NULL," +
                "  PRIMARY KEY ("+Id_usuario+"))";
        sqLiteDatabase.execSQL(CREATE_USER_TABLE);
        String CREATE_CONTA_TABLE ="CREATE TABLE  "+TABLE_CONTA+"( "+Id_conta+"INT NOT NULL AUTO_INCREMENT," +
                Id_conta_usuario+"INT NOT NULL," +
                Nome_conta+" VARCHAR(45) NOT NULL," +Saldo_conta
                +"DOUBLE NULL,CONSTRAINT "+Id_conta_usuario+" FOREIGN KEY ("+Id_usuario+")" +
                " REFERENCES " +TABLE_USER+"("+Id_usuario+"))";
        sqLiteDatabase.execSQL(CREATE_CONTA_TABLE);
        String CREATE_STOCK_TABLE="CREATE TABLE "+TABLE_STOCK +"(" +Id_stock+
                " INT NOT NULL AUTO_INCREMENT," +Id_conta_stock+
                " INT NOT NULL, PRIMARY KEY ("+Id_stock+"), CONSTRAINT "+Id_conta_stock
                +"FOREIGN KEY ("+Id_conta_stock+") REFERENCES "+TABLE_CONTA+"("+Id_conta+")";
        sqLiteDatabase.execSQL(CREATE_STOCK_TABLE);
        String CREATE_ITEM_TABLE="CREATE TABLE "+TABLE_ITEM+"(" +
                Id_itens+"INT NOT NULL AUTO_INCREMENT,"+ Id_stock_item+" INT NOT NULL," +Nome_item +
                " VARCHAR(45) NULL," +Quantidade+ "INT NULL," + Quantidade_disp+"INT NULL," +
                preco_item+"DECIMAL NULL," +data_item+"DATE NULL,PRIMARY KEY ("+Id_itens+",CONSTRAINT "+Id_stock_item+
                "FOREIGN KEY ("+Id_stock_item+") REFERENCES "+TABLE_STOCK+"("+Id_stock+"))";
        sqLiteDatabase.execSQL(CREATE_ITEM_TABLE);
        String CREATE_VENDA_TABLE=" CREATE TABLE "+TABLE_VENDA+"(" +Id_venda + "INT NOT NULL," +
                Id_stock_venda+"INT NULL,"+Id_item_venda
                +"INT NULL,"+descricao_venda+" VARCHAR(45) NULL," +
                data_venda+" DATETIME NULL," +
                valor_venda+" DECIMAL NULL,PRIMARY KEY ("+Id_venda+"),CONSTRAINT "+Id_stock_venda+" FOREIGN KEY ("
                +Id_stock+" ) REFERENCES "+TABLE_STOCK+"("+Id_stock+"),CONSTRAINT "+Id_item_venda+" FOREIGN KEY ("+Id_item_venda+
                "REFERENCES"+TABLE_VENDA+ "("+Id_venda+")";
        sqLiteDatabase.execSQL(CREATE_VENDA_TABLE);
        String CREATE_TRANSACAO_TABLE="CREATE TABLE "+TABLE_TRANSACAO+"("+Id_transacao+ "INT NOT NULL AUTO_INCREMENT,"
                +Id_conta_transacao+"INT NOT NULL, PRIMARY KEY (`"+Id_transacao+"),CONSTRAINT "+Id_conta_transacao+"FOREIGN KEY ("+Id_conta_transacao+
                ") REFERENCES"+TABLE_CONTA+"("+Id_conta_transacao+"))";
        sqLiteDatabase.execSQL(CREATE_TRANSACAO_TABLE);
        String CREATE_DEPOSITO_TABLE="CREATE TABLE "+TABLE_DEPOSITO+"("+Id_deposito+
                "INT NOT NULL AUTO_INCREMENT," +Id_transicao_deposito+"INT NOT NULL," +valor_deposito+
                "DECIMAL NULL," +discricao+"VARCHAR(45) NULL,"+data_deposito +" DATETIME NULL," +Recorrencia
                +"VARCHAR45,PRIMARY KEY ("+Id_transicao_deposito+"),CONSTRAINT "+Id_transicao_deposito+"FOREIGN KEY ("+Id_transicao_deposito+") REFERENCES "+TABLE_TRANSACAO+
                "("+Id_transacao+"))";
        sqLiteDatabase.execSQL(CREATE_DEPOSITO_TABLE);
        String CREATE_DEPESA_TABLE="CREATE TABLE"+TABLE_DESPESA+"("+Id_despesa+
                "INT NOT NULL AUTO_INCREMENT," +Id_transicao_deposito+"INT NOT NULL," +valor_despesa+
                "DECIMAL NULL," +discricao_despesa+"VARCHAR(45) NULL,"+data_despesa +" DATETIME NULL," +Recorrencia_despesa
                +"VARCHAR45,PRIMARY KEY ("+Id_transicao_despesa+"),CONSTRAINT "+Id_despesa+"FOREIGN KEY ("+Id_transicao_despesa+") REFERENCES "+TABLE_TRANSACAO+
                "("+Id_transacao+"))";
        sqLiteDatabase.execSQL(CREATE_DEPESA_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTA);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_DEPOSITO);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_DESPESA);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_VENDA);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEM);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_STOCK);
        onCreate(sqLiteDatabase);

    }
    public void copiarEverificarBasedb(){
        boolean dbExiste=verificar_db();
        if (dbExiste){
            Log.d("TAG","Base De Dados Existe");
        }
        else {
            this.getReadableDatabase();
        }
        try {
            copiardb();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("TAG","Erro copiando DB");
        }
    }

    public boolean verificar_db(){
        SQLiteDatabase verdb=null;
        String mypath=DB_PATH+DB_NAME;
        verdb=SQLiteDatabase.openDatabase(mypath,null,SQLiteDatabase.OPEN_READWRITE);
        if (verdb!=null){
            verdb.close();
        }
        return verdb != null ? true:false;
    }
    public void copiardb() throws IOException {
        InputStream inputStream=myContext.getAssets().open(DB_NAME);
        String nomedoficheiro =DB_PATH+DB_NAME;
        OutputStream outputStream=new FileOutputStream(nomedoficheiro);
        byte[]buffer=new byte[1024];
        int lenght;
        while ((lenght=inputStream.read(buffer))>0){
            outputStream.write(buffer,0,lenght);
        }
        outputStream.flush();
        outputStream.close();
        inputStream.close();
    }

    public synchronized void close(){
        if (database !=null){
            database.close();
        }
    }

    //CRIAR USUARIO
    public void REGISTRAR_USUARIO (int telefone_Usuario,int pin,String nome_usuario){
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        ContentValues values=new ContentValues();
        values.put(Nome_usuario,nome_usuario);
        values.put(Pin,pin);
        values.put(Telefone_usuario,telefone_Usuario);
        sqLiteDatabase.insert(TABLE_USER,null,values);
        sqLiteDatabase.close();
    }
    //CRIAR CONTA
    public void CRIAR_CONTA(String nome_conta){
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        ContentValues values=new ContentValues();
        values.put(Nome_conta,nome_conta);
        values.put(Saldo_conta,0.00);
        sqLiteDatabase.insert(TABLE_CONTA,null,values);
        sqLiteDatabase.close();
    }
    //CRIAR ITEM
    public void ADD_ITEM(String nome_item,int QuantidadeItem,int QuantidadeDisp,double PrecoItem){
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        ContentValues values=new ContentValues();
        Date hoje=new Date();
        values.put(Nome_item,nome_item);
        values.put(Quantidade,QuantidadeItem);
        values.put(Quantidade_disp,QuantidadeDisp);
        values.put(preco_item,PrecoItem);
        values.put(data_item, hoje.getDay()+"/"+hoje.getMonth()+"/"+hoje.getYear());

        sqLiteDatabase.insert(TABLE_ITEM,null,values);
        sqLiteDatabase.close();
    }
    //CRIAR DEPOSITO
    public void ADD_DEPOSITO (String discricao_despesa,double valor,String recorrencia){
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        ContentValues values=new ContentValues();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("YYYY-MM-DD hh:mm:ss");
        String datetime = dateformat.format(c.getTime());
        values.put(discricao,discricao_despesa);
        values.put(valor_deposito,valor);
        values.put(Recorrencia,recorrencia);
        values.put(data_deposito,datetime);
        sqLiteDatabase.insert(TABLE_DEPOSITO,null,values);
        sqLiteDatabase.close();
    }
    //CRIAR DESPESA
    public void ADD_DESPESA (String discricao_despesa,double valor,String recorrencia){
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        ContentValues values=new ContentValues();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("YYYY-MM-DD hh:mm:ss");
        String datetime = dateformat.format(c.getTime());
        values.put(discricao_despesa,discricao_despesa);
        values.put(valor_despesa,valor);
        values.put(Recorrencia_despesa,recorrencia);
        values.put(data_despesa,datetime);
        sqLiteDatabase.insert(TABLE_DESPESA,null,values);
        sqLiteDatabase.close();
    }

    public Cursor Querydata(String query){
        return database.rawQuery(query,null);
    }

}

