package com.example.irhen.segments;
/*
Данная программа реализуется проверку видимости "камере", установленной в точке (0, 0), последнего
введеного отрезка.
 */

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private Button save, find, exit;
    private EditText x1, y1, x2, y2;
    private StringBuffer sb = new StringBuffer();
    private int flag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addOnButtonClickListener();
    }

    public void addOnButtonClickListener(){
        save = (Button) findViewById(R.id.save);
        find = (Button) findViewById(R.id.find);
        exit = (Button) findViewById(R.id.exit);
        x1 = (EditText) findViewById(R.id.x1);
        y1 = (EditText) findViewById(R.id.y1);
        x2 = (EditText) findViewById(R.id.x2);
        y2 = (EditText) findViewById(R.id.y2);

        //Вызов метода сохранения в буфер данных. При это проверяется заполнены ли все поля.
        save.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (
                                x1.getText().toString().isEmpty()||
                                        y1.getText().toString().isEmpty()||
                                        x2.getText().toString().isEmpty()||
                                        y2.getText().toString().isEmpty()
                                ){
                            AlertDialog.Builder builderFlag = new AlertDialog.Builder(MainActivity.this);

                            builderFlag.setMessage("Проверьте правильность ввода координат!")
                                    .setCancelable(false)
                                    .setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                        }
                                    });
                            AlertDialog alertDialog = builderFlag.create();
                            alertDialog.setTitle("Ошибка!");
                            alertDialog.show();
                        }else {
                            saveData(sb);
                        }


                    }
                }
        );
        //Вызов метода проверки видимости отрезка
        find.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        findData(sb);
                    }
                }
        );
        //Выход из приложения
        exit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("Выйти из приложения?")
                                .setCancelable(false)
                                .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                })
                                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        finish();
                                    }
                                });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.setTitle("Закрытие приложения");
                        alertDialog.show();
                    }
                }
        );
    }
    /*
    Метод, вызывающийся по нажатию кнопки "Внести запись". Сохраняет введенные координаты в буффер и
    обнуляет поля ввода координат.
     */
    public void saveData(StringBuffer sb){
        this.sb = sb;
        sb.append(x1.getText().toString());
        sb.append(",");
        sb.append(y1.getText().toString());
        sb.append(",");
        sb.append(x2.getText().toString());
        sb.append(",");
        sb.append(y2.getText().toString());
        sb.append(";");
        x1.setText("");
        y1.setText("");
        x2.setText("");
        y2.setText("");
    }
    /*
    Метод, вызывающийся по нажатию кнопки "Проверка видимости". Данные из буффера заносятся в двухмерный
    строковый массив, из которого в дальнейшем данные для проверки конвертируются в целые числа. Для
    проверки видимости проводим два отрезка: от "камеры" (из начала координат) к началу и к концу
    контрольного отрезка. Затем проверяем на пересечение соединяющих отрезков другими отрезками. Если оба
     соединяющих отрезка пересекаются хотя бы одн раз другими отрезками - контрольный отрезок не виден.
     Если один из соединяющих отрезов не пересекается - контрольный отрезок виден "камере".
     */
    public void findData(StringBuffer sb){
        this.sb = sb;
        flag = 0;
        String[] mas = sb.toString().trim().split(";");
        String[][] result = new String[mas.length][];
        for (int i = 0; i < mas.length; ++i){
            result[i] = mas[i].toString().trim().split(",");
        }
        //проверяем начало последнего отрезка на пересечение со всеми линиями
        Point  p1 = new Point(0,0);
        Point  p2 = new Point(Integer.parseInt(result[result.length-1][0]),Integer.parseInt(result[result.length-1][1]));
        for (int i = 0; i < result.length-1; ++i){
            Point p3 = new Point(Integer.parseInt(result[i][0]), Integer.parseInt(result[i][1]));
            Point p4 = new Point(Integer.parseInt(result[i][2]), Integer.parseInt(result[i][3]));
            if (checkIntersection(p1, p2, p3, p4) == false)
                flag++;
        }

        //проверяем конец последнего отрезка на пересечение со всеми линиями
        Point  p5 = new Point(0,0);
        Point  p6 = new Point(Integer.parseInt(result[result.length-1][2]),Integer.parseInt(result[result.length-1][3]));
        for (int i = 0; i < result.length-1; ++i){
            Point  p7 = new Point(Integer.parseInt(result[i][0]),Integer.parseInt(result[i][1]));
            Point  p8 = new Point(Integer.parseInt(result[i][2]),Integer.parseInt(result[i][3]));
            if (checkIntersection (p5, p6, p7, p8) == false)
                flag++;
        }

        if (flag == 0 && result.length > 1) {
            AlertDialog.Builder builderFlag = new AlertDialog.Builder(MainActivity.this);

            builderFlag.setMessage("Контрольный отрезок не виден")
                    .setCancelable(false)
                    .setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    });
            AlertDialog alertDialog = builderFlag.create();
            alertDialog.setTitle("Результат работы программы");
            alertDialog.show();
        }
        else if (result.length == 1)
        {
            AlertDialog.Builder builderFlag = new AlertDialog.Builder(MainActivity.this);

            builderFlag.setMessage("Введен один отрезок. Отрезок виден!")
                    .setCancelable(false)
                    .setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    });
            AlertDialog alertDialog = builderFlag.create();
            alertDialog.setTitle("Результат работы программы");
            alertDialog.show();
        }
        else{
            AlertDialog.Builder builderFlag = new AlertDialog.Builder(MainActivity.this);

            builderFlag.setMessage("Контрольный отрезок виден!")
                    .setCancelable(false)
                    .setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    });
            AlertDialog alertDialog = builderFlag.create();
            alertDialog.setTitle("Результат работы программы");
            alertDialog.show();
        }


    }

    /*
    Метод, проверяющий пересечение двух отрезков
     */
    private boolean checkIntersection(Point p1, Point p2,Point p3, Point p4) {

        // расставим точки по порядку, т.е. p1.x <= p2.x
        if (p2.x < p1.x) {

            Point tmp = p1;
            p1 = p2;
            p2 = tmp;
        }
        //p3.x <= p4.x
        if (p4.x < p3.x) {

            Point tmp = p3;
            p3 = p4;
            p4 = tmp;
        }

        //проверим существование потенциального интервала для точки пересечения отрезков
        if (p2.x < p3.x) {
            return false;
        }

        //если оба отрезка вертикальные
        if((p1.x - p2.x == 0) && (p3.x - p4.x == 0)) {

            //если они лежат на одном X
            if(p1.x == p3.x) {

                //проверим пересекаются ли они, т.е. есть ли у них общий Y
                //для этого возьмём отрицание от случая, когда они НЕ пересекаются
                if (!((Math.max(p1.y, p2.y) < Math.min(p3.y, p4.y)) ||
                        (Math.min(p1.y, p2.y) > Math.max(p3.y, p4.y)))) {

                    return true;
                }
            }

            return false;
        }

        //если первый отрезок вертикальный
        if (p1.x - p2.x == 0) {

            //найдём Xa, Ya - точки пересечения двух прямых
            double Xa = p1.x;
            double A2 = (p3.y - p4.y) / (p3.x - p4.x);
            double b2 = p3.y - A2 * p3.x;
            double Ya = A2 * Xa + b2;

            if (p3.x <= Xa && p4.x >= Xa && Math.min(p1.y, p2.y) <= Ya &&
                    Math.max(p1.y, p2.y) >= Ya) {

                return true;
            }

            return false;
        }

        //если второй отрезок вертикальный
        if (p3.x - p4.x == 0) {

            //найдём Xa, Ya - точки пересечения двух прямых
            double Xa = p3.x;
            double A1 = (p1.y - p2.y) / (p1.x - p2.x);
            double b1 = p1.y - A1 * p1.x;
            double Ya = A1 * Xa + b1;

            if (p1.x <= Xa && p2.x >= Xa && Math.min(p3.y, p4.y) <= Ya &&
                    Math.max(p3.y, p4.y) >= Ya) {

                return true;
            }

            return false;
        }

        //оба отрезка невертикальные
        double A1 = (p1.y - p2.y) / (p1.x - p2.x);
        double A2 = (p3.y - p4.y) / (p3.x - p4.x);
        double b1 = p1.y - A1 * p1.x;
        double b2 = p3.y - A2 * p3.x;

        if (A1 == A2) {
            return false; //отрезки параллельны
        }

        //Xa - абсцисса точки пересечения двух прямых
        double Xa = (b2 - b1) / (A1 - A2);

        if ((Xa < Math.max(p1.x, p3.x)) || (Xa > Math.min( p2.x, p4.x))) {
            return false; //точка Xa находится вне пересечения проекций отрезков на ось X
        }
        else {
            return true;
        }
    }
}
