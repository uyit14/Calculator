package com.example.uy.a14110230_taibaouy_calculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


class InfixToPostfix{
    boolean check_error = false; //kiem tra ki tu dau tien la am hay duong, kiem tra loi


    //chuan hoa so
    public String standardizeDouble(double num){
        int a = (int)num;
        if (a == num)
            return Integer.toString(a);
        else return Double.toString(num);
    }

    //Kiem tra ki tu c co phai la so khong
    public boolean isNum(char c){
        if(Character.isDigit(c)) return true;
        else return  false;
    }

    //Chuyen so sang chuoi
    public String numTOstring(double num){
        return standardizeDouble(num);
    }

    //Chuyen chuoi sang so
    public double stringTOnum(String s){
        return Double.parseDouble(s);
    }

    //Kiem tra xem co phai toan tu
    public boolean isOperator(char c){
        char operator[] = {'+', '-', '*', '/', '~', '(', ')'}; //~ thay cho dau am
        Arrays.sort(operator);
        if(Arrays.binarySearch(operator, c) > -1)
            return true;
        else return false;
    }

    //Thiet lap thu tu uu tien
    public int priority(char c){
        switch (c){
            case '+' : case '-' : return 1;
            case '*' : case '/' : return 2;
            case '~' : return 3;
        }
        return 0;
    }

    //Kiem tra toan tu 1 ngoi
    public boolean isOneMath(char c){
        char operator[] = {'(', '~'}; //
        Arrays.sort(operator);
        if(Arrays.binarySearch(operator, c) > -1)
            return true;
        else  return false;
    }

    //Chuan hoa bieu thuc
    public  String standardize(String s) {
        String s1 = "";
        s = s.trim();
        s = s.replaceAll("\\s+", " "); //chuan hoa s
        int open = 0, close = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '(') open++;
            if (c == ')') close++;
        }
        for (int i = 0; i < (open - close); i++) {
            s += ')';
        }
        for (int i = 0; i < s.length(); i++) {
            if (i > 0 && isOneMath(s.charAt(i)) && (s.charAt(i - 1) == ')' || isNum(s.charAt(i - 1))))
                s1 = s1 + "*"; //chuan ")(" thanh ")*("
            if ((i == 0 || (i > 0 && !isNum(s.charAt(i - 1)))) && s.charAt(i) == '-' && isNum(s.charAt(i + 1))) {
                s1 = s1 + "~"; // check so am
            }
            else if ((i == 0 || (i>0 && !isNum(s.charAt(i-1)))) && s.charAt(i) == '+' && isNum(s.charAt(i+1))) {
                s1 = s1 + ""; // check dau +
            }
            else s1 = s1 + s.charAt(i);
        }
        return s1;
    }
    //Xu ly bieu thuc nhap vao thanh cac phan tu
    public String[] processString(String sMath){
        String s1 = "", elementMath[] = null;
        sMath = standardize(sMath);
        InfixToPostfix  ITP = new InfixToPostfix();
        for (int i=0; i<sMath.length(); i++){
            char c = sMath.charAt(i);
            if (!ITP.isOperator(c))
                s1 = s1 + c;
            else s1 = s1 + " " + c + " ";
        }
        s1 = s1.trim();
        s1 = s1.replaceAll("\\s+"," "); //	chuan hoa s1
        elementMath = s1.split(" "); //tach s1 thanh cac phan tu
        return elementMath;
    }

    //
    public String[] postfix(String[] elementMath){
        InfixToPostfix  ITP = new InfixToPostfix();
        String s1 = "", E[];
        Stack<String> S = new Stack<String>();
        for (int i=0; i<elementMath.length; i++){ 	// duyet cac phan tu
            char c = elementMath[i].charAt(0);		// c la ky tu dau tien cua moi phan tu

            if (!ITP.isOperator(c)) 				// neu c khong la toan tu
                s1 = s1 + elementMath[i] + " ";		// xuat elem vao s1
            else{									// c la toan tu
                if (c == '(') S.push(elementMath[i]);	// c la "(" -> day phan tu vao Stack
                else{
                    if (c == ')'){						// c la ")"
                        char c1;						//duyet lai cac phan tu trong Stack
                        do{
                            c1 = S.peek().charAt(0);	// c1 la ky tu dau tien cua phan tu
                            if (c1 != '(') s1 = s1 + S.peek() + " "; 	// trong khi c1 != "("
                            S.pop();
                        }while (c1 != '(');
                    }
                    else{
                        // Stack khong rong va trong khi phan tu trong Stack co do uu tien >= phan tu hien tai
                        while (!S.isEmpty() && ITP.priority(S.peek().charAt(0)) >= ITP.priority(c))
                            s1 = s1 + S.pop() + " ";
                        S.push(elementMath[i]); // 	dua phan tu hien tai vao Stack
                    }
                }
            }
        }
        while (!S.isEmpty()) s1 = s1 + S.pop() + " "; // Neu Stack con phan tu thi day het vao s1
        E = s1.split(" ");	//	tach s1 thanh cac phan tu
        return E;
    }

    //
    public String valueMath(String[] elementMath){
        Stack <Double> S = new Stack<Double>();
        InfixToPostfix  ITP = new InfixToPostfix();
        double num = 0.0;
        for (int i=0; i<elementMath.length; i++){
            char c = elementMath[i].charAt(0);
            if (!ITP.isOperator(c)) S.push(Double.parseDouble(elementMath[i])); //so
            else{	// toan tu

                double num1 = S.pop();
                switch (c) {
                    case '~' : num = -num1; break;
                    default : break;
                }
                if (!S.empty()){
                    double num2 = S.peek();
                    switch (c) {
                        //-----------------------
                        case '+' : num = num2 + num1; S.pop(); break;
                        case '-' : num = num2 - num1; S.pop(); break;
                        case '*' : num = num2 * num1; S.pop(); break;
                        case '/' : {
                            if (num1 != 0) num = num2 / num1;
                            else check_error = true;
                            S.pop(); break;
                        }
                    }
                }
                S.push(num);
            }

        }
        return numTOstring(S.pop());
    }
}

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    String textMath = "", textAns = "0", screenTextMath = "";
    int checkSubmit = 0;
    private TextView tvResult;
    private Button btnreset, btnx, btndel, btn7, btn8, btn9,
            btntru, btn4, btn5, btn6, btncong, btn1, btn2, btn3, btnngoac,
            btn0, btncham, btnct, btnbang, btnchia;

    //Error Math Error
    public void  error(){
        tvResult.setText("Math Error!");
        textAns = textMath = screenTextMath="";
    }

    //Khi bam dau bang thi goi len day
    public void submit(String[] elementMath){
        InfixToPostfix  ITP = new InfixToPostfix();
        if (textMath.length()>0){
            try{
                if (!ITP.check_error) elementMath = ITP.processString(textMath);		//	tach bieu thuc thanh cac phan tu
                if (!ITP.check_error) elementMath = ITP.postfix(elementMath);		// 	dua cac phan tu ve dang postfix
                if (!ITP.check_error) textAns = ITP.valueMath(elementMath);		//lay gia tri
                tvResult.setText(textMath+"\n="+textAns);
                textMath = textAns;
                screenTextMath = textAns;
                checkSubmit = 1;
            }catch(Exception e){
                error();
            }
            if (ITP.check_error) error();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initWidget();
        setEventClick();
    }

    //
    public void initWidget(){
        tvResult = (TextView) findViewById(R.id.tvResult);
        btn0 = (Button) findViewById(R.id.btn0);
        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        btn3 = (Button) findViewById(R.id.btn3);
        btn4 = (Button) findViewById(R.id.btn4);
        btn5 = (Button) findViewById(R.id.btn5);
        btn6 = (Button) findViewById(R.id.btn6);
        btn7 = (Button) findViewById(R.id.btn7);
        btn8 = (Button) findViewById(R.id.btn8);
        btn9 = (Button) findViewById(R.id.btn9);
        btnbang = (Button) findViewById(R.id.btnbang);
        btncham = (Button) findViewById(R.id.btncham);
        btncong = (Button) findViewById(R.id.btncong);
        btnct = (Button) findViewById(R.id.btnct);
        btndel = (Button) findViewById(R.id.btndel);
        btnngoac = (Button) findViewById(R.id.btnngoac);
        btnreset = (Button) findViewById(R.id.btnreset);
        btntru = (Button) findViewById(R.id.btntru);
        btnx = (Button) findViewById(R.id.btnx);
        btnchia = (Button) findViewById(R.id.btnchia);
    }

    //set onclick event
    public  void setEventClick(){
        //set onclick
        btn0.setOnClickListener(this);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);
        btn8.setOnClickListener(this);
        btn9.setOnClickListener(this);
        btnbang.setOnClickListener(this);
        btncham.setOnClickListener(this);
        btncong.setOnClickListener(this);
        btnct.setOnClickListener(this);
        btndel.setOnClickListener(this);
        btnngoac.setOnClickListener(this);
        btnreset.setOnClickListener(this);
        btntru.setOnClickListener(this);
        btnx.setOnClickListener(this);
        btnchia.setOnClickListener(this);
    }

    //Onclick
    public void onClick(View v){
        String elementMath[]=null;
        switch (v.getId()){
            case R.id.btn0:
                if (screenTextMath.length()<48) {	//neu bieu thuc nhap vao <48 ky tu
                    textMath += "0";
                    screenTextMath += "0";
                }
                tvResult.setText(screenTextMath);
                break;
            case R.id.btn1:
                if (screenTextMath.length()<48) {
                    textMath += "1";
                    screenTextMath += "1";
                }
                tvResult.setText(screenTextMath);
                break;
            case R.id.btn2:
                if (screenTextMath.length()<48) {
                    textMath += "2";
                    screenTextMath += "2";
                }
                tvResult.setText(screenTextMath);
                break;
            case R.id.btn3:
                if (screenTextMath.length()<48) {
                    textMath += "3";
                    screenTextMath += "3";
                }
                tvResult.setText(screenTextMath);
                break;
            case R.id.btn4:
                if (screenTextMath.length()<48) {
                    textMath += "4";
                    screenTextMath += "4";
                }
                tvResult.setText(screenTextMath);
                break;
            case R.id.btn5:
                if (screenTextMath.length()<48) {
                    textMath += "5";
                    screenTextMath += "5";
                }
                tvResult.setText(screenTextMath);
                break;
            case R.id.btn6:
                if (screenTextMath.length()<48) {
                    textMath += "6";
                    screenTextMath += "6";
                }
                tvResult.setText(screenTextMath);
                break;
            case R.id.btn7:
                if (screenTextMath.length()<48) {
                    textMath += "7";
                    screenTextMath += "7";
                }
                tvResult.setText(screenTextMath);
                break;
            case R.id.btn8:
                if (screenTextMath.length()<48) {
                    textMath += "8";
                    screenTextMath += "8";
                }
                tvResult.setText(screenTextMath);
                break;
            case R.id.btn9:
                if (screenTextMath.length()<48) {
                    textMath += "9";
                    screenTextMath += "9";
                }
                tvResult.setText(screenTextMath);
                break;
            case R.id.btncham:
                if (screenTextMath.length()<48) {
                    textMath += ".";
                    screenTextMath += ".";
                }
                tvResult.setText(screenTextMath);
                break;
            case R.id.btncong:
                if (screenTextMath.length()<48) {
                    textMath += "+";
                    screenTextMath += "+";
                }
                tvResult.setText(screenTextMath);
                break;
            case R.id.btntru:
                if (screenTextMath.length()<48) {
                    textMath += "-";
                    screenTextMath += "-";
                }
                tvResult.setText(screenTextMath);
                break;
            case R.id.btnx:
                if (screenTextMath.length()<48) {
                    textMath += "*";
                    screenTextMath += "*";
                }
                tvResult.setText(screenTextMath);
                break;
            case R.id.btnchia:
                if (screenTextMath.length()<48) {
                    textMath += "/";
                    screenTextMath += "/";
                }
                tvResult.setText(screenTextMath);
                break;
            case R.id.btnct:
                InfixToPostfix ITP = new InfixToPostfix();
                String str="";
                String str2="";
                int count;
                for(int i=screenTextMath.length()-1; i>=0; i--){
                    //Phan tu cuoi cung la Operator
                    if(ITP.isOperator(screenTextMath.charAt(screenTextMath.length()-1))){
                        //...3+
                        if(screenTextMath.charAt(screenTextMath.length()-1)=='+'){
                            screenTextMath = screenTextMath.substring(0,screenTextMath.length()-1);
                            textMath = textMath.substring(0,textMath.length()-1);
                            textMath += "-";
                            screenTextMath += "-";
                            break;
                        }

                        //...3-
                        if(screenTextMath.charAt(screenTextMath.length()-1)=='-'){
                            screenTextMath = screenTextMath.substring(0,screenTextMath.length()-1);
                            textMath = textMath.substring(0,textMath.length()-1);
                            textMath += "+";
                            screenTextMath += "+";
                            break;
                        }
                    }
                    //
                    else{
                        if(ITP.isOperator(screenTextMath.charAt(i))){
                            count = i;
                            for(int j=count+1;j<screenTextMath.length();j++){
                                str += screenTextMath.charAt(j);
                            }
                            if((screenTextMath.charAt(i)=='*') || (screenTextMath.charAt(i)=='/')){
                                str2 +="(";
                                str2 +="-";
                                screenTextMath = screenTextMath.substring(0,i+1);
                                textMath = textMath.substring(0,i+1);
                                textMath += str2;
                                textMath += str;
                                screenTextMath += str2;
                                screenTextMath += str;
                                break;
                            }
                            if(screenTextMath.charAt(i)=='-'){
                                screenTextMath = screenTextMath.substring(0,i);
                                textMath = textMath.substring(0,i);
                                textMath += "+";
                                textMath += str;
                                screenTextMath += "+";
                                screenTextMath += str;
                                break;
                            }
                            if(screenTextMath.charAt(i)=='+'){
                                screenTextMath = screenTextMath.substring(0,i);
                                textMath = textMath.substring(0,i);
                                textMath += "-";
                                textMath += str;
                                screenTextMath += "-";
                                screenTextMath += str;
                                break;
                            }
                        }
                    }


                }
                tvResult.setText(screenTextMath);
                break;
            case R.id.btnngoac:
                InfixToPostfix ITP2 = new InfixToPostfix();
                if(screenTextMath.isEmpty()){
                    textMath+="(";
                    screenTextMath+="(";
                    tvResult.setText(screenTextMath);
                }
                else if(ITP2.isOperator(screenTextMath.charAt(screenTextMath.length()-1))
                        && screenTextMath.charAt(screenTextMath.length()-1)!=')'){
                    textMath+="(";
                    screenTextMath+="(";
                    tvResult.setText(screenTextMath);
                }
                else {
                    textMath+=")";
                    screenTextMath+=")";
                    tvResult.setText(screenTextMath);
                }
                break;
            case R.id.btnbang:
                addOperation(tvResult.getText().toString());
                addNumber(tvResult.getText().toString());
                if(arrOperation.size()==1 && arrNumber.size()==1){
                    switch (arrOperation.get(0)){
                        case "+":
                            screenTextMath +="";
                            textMath +="";
                            tvResult.setText(textMath);
                            break;
                        case "-":
                            screenTextMath +="";
                            textMath +="";
                            tvResult.setText(textMath);
                            break;
                        case "*":
                            error();
                            break;
                        case "/":
                            error();
                            break;
                        default:
                            break;
                    }
                }
                else
                    submit(elementMath);
                break;
            case R.id.btnreset:
                textMath="";
                screenTextMath="";
                textAns="0";
                tvResult.setText(screenTextMath);
                break;
            case R.id.btndel:
                if (tvResult.length()>0){
                    char c = textMath.charAt(textMath.length()-1);
                    if (textMath.length() > 1 && c == '(' && textMath.charAt(textMath.length()-2) == '^'){
                        screenTextMath = screenTextMath.substring(0,screenTextMath.length()-2);
                        textMath = textMath.substring(0,textMath.length()-2);
                    }
                    else if (textMath.length() > 1 && c == '(' && (textMath.charAt(textMath.length()-2) == 's' || textMath.charAt(textMath.length()-2) == 'c' || textMath.charAt(textMath.length()-2) == 't') ){
                        textMath = textMath.substring(0,textMath.length()-2);
                        screenTextMath = screenTextMath.substring(0,screenTextMath.length()-4);
                    }
                    else {
                        textMath = textMath.substring(0,textMath.length()-1);
                        screenTextMath = screenTextMath.substring(0,screenTextMath.length()-1);
                    }
                }
                tvResult.setText(screenTextMath);
                break;
            default:
                break;
        }
    }

    //ArrayLst chua phep tinh
    public ArrayList<String> arrOperation;

    //ArrayLst chua so
    public  ArrayList<Double> arrNumber;

    //Lấy tất cả phép tính lưu vào mảng arrOperation
    public  int addOperation(String input){
        arrOperation = new ArrayList<>();
        arrNumber = new ArrayList<>();
        char[] cArray = input.toCharArray(); //lấy ra từng kí tự và lưu vào mảng cArray
        for(int i=0; i<cArray.length;i++){
            switch (cArray[i]){
                case '+':
                    arrOperation.add(cArray[i] + ""); //có dấu cộng thì lưu vào mảng arrOperation
                    break;
                case '-':
                    arrOperation.add(cArray[i] + "");
                    break;
                case '*':
                    arrOperation.add(cArray[i] + "");
                    break;
                case '/':
                    arrOperation.add(cArray[i] + "");
                    break;
                default:
                    break;

            }
        }
        return 0;
    }

    //Lấy tất cả các số lưu vào mảng arrNumber
    public  void addNumber (String strInput){
        //arrNumber = new ArrayList<>();
        Pattern regex = Pattern.compile("(\\d+(?:\\.\\d+)?)"); //Xem nhập vào đúng định dạng không? (định dạng lấy ra các con số)
        Matcher matcher = regex.matcher(strInput);
        while (matcher.find()){
            arrNumber.add(Double.valueOf(matcher.group(1)));
        }
    }

    //Hàm xóa kí tự bất kì trong chuôi
    public String DeleteNChar(String strInput, int n) {
        return strInput.length() <= 0 || n > strInput.length() ? "" : strInput.substring(0, strInput.length() - n);
    }
}
