import java.awt.*;
import java.awt.event.*;
import java.math.*;
import java.util.*;
import javax.swing.*;

class JCalculator extends JFrame implements ActionListener{
	static final long serialVersionUID = 1L;

	JCalculator(){
		init();
		setLayout(new BorderLayout());
		setText();
		setButton();
		setKey();
	}

	void init(){
		fnum=BigDecimal.ZERO;
		snum=BigDecimal.ZERO;
		buffer="0";
		oprt=0;
		isF=false;
		isV=false;
		isE=true;
	}

	boolean calc(){
		if(oprt==0){
			fnum=snum;
		}else if(oprt==1){
			fnum=fnum.add(snum);
		}else if(oprt==2){
			fnum=fnum.subtract(snum);
		}else if(oprt==3){
			fnum=fnum.multiply(snum);
		}else if(oprt==4){
			try{
				fnum=fnum.divide(snum,200,RoundingMode.HALF_EVEN);
			}catch(ArithmeticException ex){
				init();
				text.setText("Error!");
				return false;
			}
		}else if(oprt==5){
			try{
				fnum=fnum.remainder(snum);
			}catch(ArithmeticException ex){
				init();
				text.setText("Error!");
				return false;
			}
		}
		return true;
	}

	void show(BigDecimal num){
		if(((num.stripTrailingZeros()).toPlainString()).length()<=24){
			text.setText((num.stripTrailingZeros()).toPlainString());
		}else{
			BigDecimal topv=BigDecimal.ONE,tmp=num;
			if(num.signum()<0) tmp=tmp.multiply(BigDecimal.valueOf(-1D));
			if(tmp.compareTo(BigDecimal.ONE)>0){
				while(tmp.compareTo(BigDecimal.ONE)>0){
					tmp=tmp.divide(BigDecimal.TEN);
					topv=topv.multiply(BigDecimal.TEN);
				}
			}else{
				int tcnt=0;
				while(tmp.compareTo(BigDecimal.ONE)<0){
					tmp=tmp.multiply(BigDecimal.TEN);
					topv=topv.divide(BigDecimal.TEN);
					tcnt++;
					if(tcnt>220) break;
				}
			}
			if(num.signum()<0) tmp=tmp.multiply(BigDecimal.valueOf(-1D));
			BigDecimal show=tmp.setScale(20, RoundingMode.HALF_EVEN).multiply(topv);
			String Ans=show.stripTrailingZeros().toString();
			if(Ans.matches("0E[-0-9]+")) Ans="0";
			text.setText(Ans);
		}
	}

	void setText(){
		text=new JTextField(buffer,26);
		text.setFocusable(false);
		text.setHorizontalAlignment(JTextField.RIGHT);
		text.setFont(new Font("Consolas",Font.PLAIN,18));

		JPanel jpTF=new JPanel();
		jpTF.setBorder(BorderFactory.createEmptyBorder(5, 10, 0, 10));
		jpTF.add(text);

		add(jpTF,BorderLayout.NORTH);

		isM=new JLabel();
		isM.setHorizontalAlignment(JLabel.CENTER);
	}
 
	void setButton(){
		num=new JButton[10];
		for(int i=0;i<10;i++){
			num[i]=new JButton(""+i);
			num[i].setFocusable(false);
			num[i].addActionListener(this);
		}
		
		ButtonGroup bg=new ButtonGroup();
		bg.add(dot=new JButton("."));
		bg.add(sign=new JButton("±"));
		bg.add(plus=new JButton("+"));
		bg.add(subtract=new JButton("-"));
		bg.add(multiply=new JButton("×"));
		bg.add(divide=new JButton("÷"));
		bg.add(mod=new JButton("mod"));
		bg.add(enter=new JButton("="));
		bg.add(cls=new JButton("C"));
		bg.add(cle=new JButton("CE"));
		bg.add(back=new JButton("←"));
		bg.add(copy=new JButton("copy"));
		bg.add(square=new JButton("x²"));
		bg.add(sqrt=new JButton("√"));
		bg.add(reci=new JButton("1/x"));
		bg.add(ms=new JButton("MS"));
		bg.add(mc=new JButton("MC"));
		bg.add(mr=new JButton("MR"));
		bg.add(ma=new JButton("M+"));
		
		Enumeration<AbstractButton> bgE=bg.getElements();
		while(bgE.hasMoreElements()){
			AbstractButton tb=bgE.nextElement();
			tb.addActionListener(this);
			tb.setFocusable(false);
		}

		JPanel jpMB=new JPanel(new GridLayout(0,1,2,2));
		jpMB.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 2));

		jpMB.add(isM);
		jpMB.add(mc);
		jpMB.add(mr);
		jpMB.add(ms);
		jpMB.add(ma);

		add(jpMB,BorderLayout.WEST);

		JPanel jpNB=new JPanel(new GridLayout(0,5,2,2));
		jpNB.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));

		jpNB.add(cls);
		jpNB.add(cle);
		jpNB.add(back);
		jpNB.add(mod);
		jpNB.add(copy);

		jpNB.add(num[7]);
		jpNB.add(num[8]);
		jpNB.add(num[9]);
		jpNB.add(divide);
		jpNB.add(sqrt);

		jpNB.add(num[4]);
		jpNB.add(num[5]);
		jpNB.add(num[6]);
		jpNB.add(multiply);
		jpNB.add(square);

		jpNB.add(num[1]);
		jpNB.add(num[2]);
		jpNB.add(num[3]);
		jpNB.add(subtract);
		jpNB.add(reci);

		jpNB.add(num[0]);
		jpNB.add(dot);
		jpNB.add(sign);
		jpNB.add(plus);
		jpNB.add(enter);

		add(jpNB,BorderLayout.CENTER);

	}
	
	void setKey(){
		addKeyListener(new KeyListener(){
			public void keyPressed(KeyEvent e){
				char vk=e.getKeyChar();
				switch(vk){
				case '+': plus.doClick();break;
				case '-': subtract.doClick();break;
				case '*': multiply.doClick();break;
				case '/': divide.doClick();break;
				case '.': dot.doClick();break;
				case '_': sign.doClick();break;
				case KeyEvent.VK_EQUALS: 
				case KeyEvent.VK_ENTER: enter.doClick();break;
				}
				int vkc=e.getKeyCode();
				switch(vkc){
				case KeyEvent.VK_BACK_SPACE: back.doClick();break;
				case KeyEvent.VK_ESCAPE: cls.doClick();break;
				case KeyEvent.VK_C: if(e.isControlDown()) copy.doClick();break;
				}
			}

			public void keyReleased(KeyEvent e) {
			}

			public void keyTyped(KeyEvent e) {
				char vk=e.getKeyChar();
				switch(vk){
				case KeyEvent.VK_0:	num[0].doClick();break;
				case KeyEvent.VK_1: num[1].doClick();break;
				case KeyEvent.VK_2: num[2].doClick();break;
				case KeyEvent.VK_3: num[3].doClick();break;
				case KeyEvent.VK_4: num[4].doClick();break;
				case KeyEvent.VK_5: num[5].doClick();break;
				case KeyEvent.VK_6: num[6].doClick();break;
				case KeyEvent.VK_7: num[7].doClick();break;
				case KeyEvent.VK_8: num[8].doClick();break;
				case KeyEvent.VK_9: num[9].doClick();break;
				}
			}
		});
	}
	
	void oprtInput(String bac){
		for(int i=0;i<10;i++){
			if(bac.equals(num[i].getText())){
				if(isE) oprt=0;
				if(buffer.equals("0")){
					buffer=""+i;
				}else{
					buffer=buffer+i;
				}
				isV=false;
				isE=false;
				snum=new BigDecimal(buffer);
				text.setText(buffer);
				return;
			}
		}

		if(bac.equals(dot.getText())){
			if(isE) oprt=0;
			if(!isF){
				isF=true;
				buffer=buffer+".";
				snum=new BigDecimal(buffer);
				isV=false;
				isE=false;
			}
			text.setText(buffer);

		}else if(bac.equals(sign.getText())){
			if(!isV){
				System.out.println("**");
				if(isE) oprt=0;
				if(buffer.equals("0")) return;
				if(buffer.matches("-[-.0-9]+")){
					buffer=buffer.substring(1);
				}else{
					buffer="-"+buffer;
				}
				isV=false;
				isE=false;
				snum=new BigDecimal(buffer);
				text.setText(buffer);
			}

		}else if(bac.equals(back.getText())){
			if(isV) return;
			if(buffer.equals("0")) return;
			if(buffer.endsWith(".")) isF=false;
			if(buffer.matches("[-]{0,1}[0-9]{1}")) buffer="0";
			else buffer=buffer.substring(0,buffer.length()-1);
			snum=new BigDecimal(buffer);
			text.setText(buffer);

		}
	}
	
	void oprtBinary(String bac){
		if(bac.equals(plus.getText())){
			if(oprt==0) fnum=snum;
			if(!isV) calc();
			oprt=1;
			snum=fnum;
			buffer="0";
			isF=false;
			isV=true;
			isE=false;
			show(snum);

		}else if(bac.equals(subtract.getText())){
			if(oprt==0) fnum=snum;
			if(!isV) calc();
			oprt=2;
			snum=fnum;
			buffer="0";
			isF=false;
			isV=true;
			isE=false;
			show(snum);

		}else if(bac.equals(multiply.getText())){
			if(oprt==0) fnum=snum;
			if(!isV) calc();
			oprt=3;
			snum=fnum;
			buffer="0";
			isF=false;
			isV=true;
			isE=false;
			show(snum);

		}else if(bac.equals(divide.getText())){
			if(oprt==0) fnum=snum;
			if(!isV){
				if(!calc()) return;
			}
			oprt=4;
			snum=fnum;
			buffer="0";
			isF=false;
			isV=true;
			isE=false;
			show(snum);

		}else if(bac.equals(mod.getText())){
			if(oprt==0) fnum=snum;
			if(!isV){
				if(!calc()) return;
			}
			oprt=5;
			snum=fnum;
			buffer="0";
			isF=false;
			isV=true;
			isE=false;
			show(snum);

		}
	}

	void oprtUnary(String bac){
		if(bac.equals(sign.getText())){
			if(isV){
				if(isE){
					snum=fnum.multiply(BigDecimal.valueOf(-1));
				}else{
					snum=snum.multiply(BigDecimal.valueOf(-1));
				}
				show(snum);
			}
		}else if(bac.equals(square.getText())){
			BigDecimal tmp;
			if(isE){
				tmp=fnum; oprt=0;
			}else{
				tmp=snum; buffer="0";
				isF=false;
			}
			snum=tmp.multiply(tmp);
			isE=false;
			isV=true;
			show(snum);

		}else if(bac.equals(reci.getText())){
			BigDecimal tmp;
			if(isE){
				tmp=fnum; oprt=0;
			}else{
				tmp=snum; buffer="0";
				isF=false;
			}
			try{
				snum=BigDecimal.ONE.divide(tmp,180,RoundingMode.HALF_EVEN);
			}catch(ArithmeticException ex){
				init();
				text.setText("Error!");
				return;
			}
			isE=false;
			isV=true;
			show(snum);

		}else if(bac.equals(sqrt.getText())){
			BigDecimal tmp;
			if(isE){
				tmp=fnum; oprt=0;
			}else{
				tmp=snum; buffer="0";
				isF=false;
			}

			if(tmp.compareTo(BigDecimal.ZERO)<0){
				init();
				text.setText("Error!");
				return;
			}

			BigDecimal top,bot;
			if(tmp.compareTo(BigDecimal.ONE)>0){
				top=BigDecimal.ONE; bot=tmp;
			}else{
				top=tmp; bot=BigDecimal.ONE;
			}
			BigDecimal mid=tmp.divide(BigDecimal.valueOf(2),180,RoundingMode.HALF_EVEN);
			while(bot.subtract(top).compareTo(new BigDecimal("1e-150"))>=0){
				if((mid.multiply(mid)).compareTo(tmp)>=0){
					bot=mid;
				}else{
					top=mid;
				}
				mid=(top.add(bot)).divide(BigDecimal.valueOf(2),180,RoundingMode.HALF_EVEN);
			}
			snum=mid;

			isE=false;
			isV=true;
			show(snum);

		}
	}

	void oprtMem(String bac){
		if(bac.equals(ms.getText())){
			isM.setText("M");
			if(isE){
				mem=fnum;
			}else{
				mem=snum;
			}

		}else if(bac.equals(mc.getText())){
			isM.setText("");

		}else if(bac.equals(mr.getText())){
			if(isE) oprt=0;
			if(isM.getText().equals("M")){
				snum=mem;
				buffer="0";
				isV=true;
				isF=false;
				show(snum);
			}

		}else if(bac.equals(ma.getText())){
			if(isM.getText().equals("M")){
				if(isE){
					mem=mem.add(fnum);
				}else{
					mem=mem.add(snum);
				}
			}

		}
	}

	void oprtSys(String bac){
		if(bac.equals(enter.getText())){
			if(!calc()) return;
			isF=false;
			isV=true;
			isE=true;
			buffer="0";
			show(fnum);

		}else if(bac.equals(copy.getText())){
			text.selectAll();
			text.copy();

		}else if(bac.equals(cls.getText())){
			init();
			show(snum);

		}else if(bac.equals(cle.getText())){
			isF=false;
			snum=BigDecimal.ZERO;
			buffer="0";
			show(snum);

		}
	}

	public void actionPerformed(ActionEvent e){
		String bac=e.getActionCommand();

		oprtInput(bac);
		oprtBinary(bac);
		oprtUnary(bac);
		oprtMem(bac);
		oprtSys(bac);

	}

	public static void main(String args[]){
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,"System UI picked error!","UI Error!",JOptionPane.ERROR_MESSAGE);
		}
		JCalculator frame=new JCalculator();
		frame.setTitle("JCalculator");
		frame.setSize(400,240);
		frame.setResizable(false);
		frame.setVisible(true);
		frame.requestFocus();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	private JLabel isM;
	private JTextField text;
	private String buffer;
	private int oprt;
	private boolean isF,isV,isE;
	private BigDecimal fnum,snum,mem;

	private JButton[] num;
	private JButton dot,sign,enter;
	private JButton plus,subtract,multiply,divide,mod;
	private JButton square,sqrt,reci;
	private JButton cls,cle,back,copy;
	private JButton ms,mc,mr,ma;
}