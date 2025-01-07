import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class PaintBrush extends Applet {

    ArrayList<Shape> shape;

    int x1, y1, x2, y2, x, y;

    int width, height;

    boolean isFilled = false, clear = false, undo = false, isDotted = false;

    Color defaultColor = Color.RED, themeColor = Color.WHITE;

    String currentShape = "line";

    Choice selectedShape, selectedColor, selectedTheme;

    Button fillButton, clearButton, eraserButton, undoButton, pencilButton;

    Checkbox fillCheckBox, dotCheckBox;

    boolean isDrawingFlag = false;

    BufferedImage offscreenImage;
    Graphics2D offscreenGraphics;

    public void init() {

        setBackground(new Color(239, 243, 234));
        shape = new ArrayList<>();

        setLayout(null);

        int width1 = getWidth();
        int height1 = getHeight();

        offscreenImage = new BufferedImage(width1, height1, BufferedImage.TYPE_INT_ARGB);
        offscreenGraphics = offscreenImage.createGraphics();
        offscreenGraphics.setColor(getBackground());
        offscreenGraphics.fillRect(0, 0, width1, height1);

        addLabel("Theme:", 280, 20, 80, 30);
        selectedTheme = addDropDownList(280, 80, 80, 60, "Light", "Dark");

        addLabel("Select Shape:", 410, 20, 80, 30);
        selectedShape = addDropDownList(410, 80, 80, 60, "Line", "Rectangle", "Oval");

        addLabel("Select Color:", 530, 20, 80, 30);
        selectedColor = addDropDownList(530, 80, 80, 60, "Red", "Green", "Blue", "Cyan");

        getSelectedTheme(selectedTheme);
        getSelectedShape(selectedShape);
        getSelectedColor(selectedColor);

        fillCheckBox = new Checkbox("Fill Shape");
        fillCheckBox.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                if (isFilled == false) {
                    isFilled = true;
                    fillCheckBox.setBackground(Color.YELLOW);
                    fillCheckBox.setForeground(Color.BLACK);
                } else {
                    isFilled = false;
                    setComponentStyle(fillCheckBox);
                }
            }
        });

        fillCheckBox.setBounds(1510, 20, 100, 30);
        setComponentStyle(fillCheckBox);
        add(fillCheckBox);

        dotCheckBox = new Checkbox("Dotted Shape");
        dotCheckBox.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                if (isDotted == false) {
                    isDotted = true;
                    dotCheckBox.setBackground(Color.YELLOW);
                    dotCheckBox.setForeground(Color.BLACK);
                } else {
                    isDotted = false;
                    setComponentStyle(dotCheckBox);
                }
            }
        });

        dotCheckBox.setBounds(1510, 80, 100, 30);
        setComponentStyle(dotCheckBox);
        add(dotCheckBox);

        eraserButton = new Button("Eraser");
        eraserButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                currentShape = "eraser";
            }
        });

        eraserButton.setBounds(1270, 20, 80, 30);
        setComponentStyle(eraserButton);
        add(eraserButton);

        clearButton = new Button("Clear");
        clearButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                clear = true;
                repaint();
            }
        });

        clearButton.setBounds(1390, 20, 80, 30);
        setComponentStyle(clearButton);
        add(clearButton);

        undoButton = new Button("Undo");
        undoButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                undo = true;
                if (shape.size() > 0) {
                    shape.remove(shape.size() - 1);
                }
                repaint();
            }
        });

        undoButton.setBounds(1270, 80, 80, 30);
        setComponentStyle(undoButton);
        add(undoButton);

        pencilButton = new Button("Pencil");
        pencilButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                currentShape = "pencil";
            }
        });

        pencilButton.setBounds(1390, 80, 80, 30);
        setComponentStyle(pencilButton);
        add(pencilButton);

        MouseAdapter adapter = new MouseAdapter() {

            public void mousePressed(MouseEvent e) {
                x1 = e.getX();
                y1 = e.getY();
                isDrawingFlag = true;
            }

            public void mouseDragged(MouseEvent e) {
                x2 = e.getX();
                y2 = e.getY();
                width = Math.abs(x2 - x1);
                height = Math.abs(y2 - y1);

                switch (currentShape) {

                    case "eraser":
                        shape.add(new Rectangle(x2, y2, 15, 15, "eraser", false, true));
                        break;

                    case "pencil":
                        shape.add(new Rectangle(x2, y2, 15, 15, "pencil", false, true));
                        break;
                }

                isDrawingFlag = true;
                repaint();
            }

            public void mouseReleased(MouseEvent e) {

                if (isDrawingFlag) {
                    x2 = e.getX();
                    y2 = e.getY();
                    width = Math.abs(x2 - x1);
                    height = Math.abs(y2 - y1);

                    switch (currentShape) {

                        case "line":
                            shape.add(new Line(x1, y1, x2, y2, "line", isDotted));
                            break;

                        case "rectangle":
                            checkDimentions(x1, y1, x2, y2);
                            shape.add(new Rectangle(x, y, width, height, "rectangle", isDotted, isFilled));
                            break;

                        case "oval":
                            checkDimentions(x1, y1, x2, y2);
                            shape.add(new Oval(x, y, width, height, "oval", isDotted, isFilled));
                            break;
                    }

                    isDrawingFlag = false;
                    repaint();
                }
            }
        };

        this.addMouseListener(adapter);
        this.addMouseMotionListener(adapter);
    }

    public void addLabel(String label, int x, int y, int width, int height) {
        Label selectedLabel = new Label(label);
        selectedLabel.setBounds(x, y, width, height);
        setComponentStyle(selectedLabel);
        Font f = new Font("Arial", Font.BOLD, 12);
        selectedLabel.setFont(f);
        add(selectedLabel);
    }

    public Choice addDropDownList(int x, int y, int width, int height, String... options) {
        Choice choice = new Choice();
        for (String option : options) {
            choice.add(option);
        }

        choice.setBounds(x, y, width, height);
        Font f = new Font("Arial", Font.BOLD, 12);
        choice.setFont(f);
        add(choice);
        return choice;
    }

    public void getSelectedShape(Choice choice) {
        choice.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                String selectedItem = choice.getSelectedItem();

                switch (selectedItem) {

                    case "Line":
                        currentShape = "line";
                        break;

                    case "Rectangle":
                        currentShape = "rectangle";
                        break;

                    case "Oval":
                        currentShape = "oval";
                        break;
                }
            }
        });
    }

    public void getSelectedColor(Choice choice) {
        choice.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                String selectedItem = choice.getSelectedItem();

                switch (selectedItem) {

                    case "Cyan":
                        defaultColor = Color.CYAN;
                        break;

                    case "Red":
                        defaultColor = Color.RED;
                        break;

                    case "Green":
                        defaultColor = Color.GREEN;
                        break;

                    case "Blue":
                        defaultColor = Color.BLUE;
                        break;

                    case "White":
                        defaultColor = Color.WHITE;
                        break;
                }
            }
        });
    }

    public void getSelectedTheme(Choice choice) {
        choice.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                String selectedItem = choice.getSelectedItem();

                switch (selectedItem) {

                    case "Dark":
                        themeColor = new Color(52, 49, 49);
                        break;

                    case "Light":
                        themeColor = new Color(216, 239, 211);
                        break;
                }

                setBackground(themeColor);
            }
        });
    }

    public void checkDimentions(int x1, int y1, int x2, int y2) {

        if (x1 - x2 < 0) {
            x = x1;
        } else {
            x = x2;
        }

        if (y1 - y2 < 0) {
            y = y1;
        } else {
            y = y2;
        }
    }

    public void setComponentStyle(Component component) {
        component.setBackground(new Color(149, 210, 179));
        component.setForeground(Color.BLACK);
        Font f = new Font("Arial", Font.BOLD, 12);
        component.setFont(f);
    }

  

public void paint(Graphics g) {
    // Initialize offscreen image and graphics objects if not already done
    if (offscreenImage == null) {
        offscreenImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        offscreenGraphics = offscreenImage.createGraphics();
    }

    // Clear the offscreen image (this avoids residual data from previous frames)
    offscreenGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    offscreenGraphics.setColor(Color.WHITE);  // Set background color to white or any other color
    offscreenGraphics.fillRect(0, 0, getWidth(), getHeight());  // Clear the canvas

    // Draw the background and UI elements on the offscreen image
    offscreenGraphics.setColor(new Color(85, 173, 155));
    offscreenGraphics.fillRect(0, 0, getWidth(), 130);

    offscreenGraphics.setColor(Color.BLACK);
    offscreenGraphics.setFont(new Font("Bradley Hand ITC", Font.BOLD, 45));
    offscreenGraphics.drawString("Paint Brush", 800, 80);

    // Handle clearing the canvas if requested
    if (clear) {
        shape = new ArrayList<Shape>();  // Reset the shapes
        clear = false;  // Reset the clear flag
    }

    // Draw all shapes from the shape list
    for (Shape s : shape) {
        if (s.isDotted()) {
            float[] dot = {5, 5};
            offscreenGraphics.setStroke(new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 10, dot, 10));
        } else {
            offscreenGraphics.setStroke(new BasicStroke(1));  // Default stroke width
        }
        s.draw(offscreenGraphics);
    }

    // Draw the current drawing shape based on the current selection
    offscreenGraphics.setColor(defaultColor);
    if (isDrawingFlag) {
        switch (currentShape) {
            case "line":
                offscreenGraphics.drawLine(x1, y1, x2, y2);
                break;
            case "rectangle":
                checkDimentions(x1, y1, x2, y2);
                offscreenGraphics.drawRect(x, y, width, height);
                if (isFilled) {
                    offscreenGraphics.fillRect(x, y, width, height);
                }
                break;
            case "oval":
                checkDimentions(x1, y1, x2, y2);
                offscreenGraphics.drawOval(x, y, width, height);
                if (isFilled) {
                    offscreenGraphics.fillOval(x, y, width, height);
                }
                break;
        }
    }

    // Once all drawing is complete on the offscreen image, draw it on the screen
    g.drawImage(offscreenImage, 0, 0, null);
}



    abstract class Shape {

        private int x1, y1, x2, y2;

        private String shapeName;

        private boolean isFilled;

        private boolean isDotted;

        private Color color;

        public Shape() {
        }

        public Shape(int x1, int y1, int x2, int y2, String shapeName, boolean isDotted, boolean isFilled) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
            this.shapeName = shapeName;
            this.isDotted = isDotted;
            this.isFilled = isFilled;

            if (shapeName == "eraser")
                this.color = getBackground();
            else
                this.color = defaultColor;
        }
		
		public Shape(int x1, int y1, int x2, int y2, String shapeName, boolean isDotted){
		
			this.x1=x1;
			
			this.y1=y1;
			
			this.x2=x2;
			
			this.y2=y2;
			
			this.shapeName=shapeName;
			
			this.isDotted=isDotted;
			
			this.color= defaultColor;
			
		}
		
		
		public boolean isDotted () {
			
			return isDotted;
		}
		

        public int getX1() { 
		
			return x1; 
			
		}
		
        public int getY1() { 
		
			return y1; 
			
		}
        
		public int getX2() { 
		
			return x2; 
			
		}
        
		public int getY2() { 
		
			return y2; 
			
		}
        
		public String getShapeName() { 
		
			return shapeName; 
			
		}
        
		public boolean isFilled() { 
		
			return isFilled; 
			
		}
        
		public Color getColor() { 
		
			return color; 
			
		}
		
		public void setColor(Color color) { 
		
			this.color = color; 
			
		}
		


        public abstract void draw(Graphics2D g);
    }

    // Define other shapes (Line, Rectangle, Oval) with their draw methods
	
	class Line extends Shape {
		
        public Line(){}
		
		public Line(int x1,int y1, int x2,int y2, String shapeName ,  boolean isDotted){
			
			super(x1, y1, x2, y2, shapeName, isDotted);
			
		}

        @Override
        public void draw(Graphics2D g2d) {
			
            g2d.setColor(getColor());
			
            g2d.drawLine(getX1(), getY1(), getX2(), getY2());
			
        }
		
    }

    
    class Rectangle extends Shape {
		
        public Rectangle(){}
		
		public Rectangle(int x1,int y1, int x2,int y2, String shapeName,  boolean isDotted, boolean isFilled){
			
			super(x1, y1, x2, y2, shapeName, isDotted,  isFilled);
			
		}

        @Override
        public void draw(Graphics2D g2d) {
			
            g2d.setColor(getColor());
			
			if (isFilled()) {
				
                g2d.fillRect(getX1(), getY1(), getX2(), getY2());
				
            }
			
            g2d.drawRect(getX1(), getY1(), getX2(), getY2());
			
            
        }
    }

   
   
    class Oval extends Shape {
		
        public Oval(){}
		
		public Oval(int x1,int y1, int x2,int y2, String shapeName,  boolean isDotted, boolean isFilled){
			
			super(x1, y1, x2, y2, shapeName, isDotted, isFilled);
			
		}

        @Override
        public void draw(Graphics2D g2d) {
			
            g2d.setColor(getColor());
			
			if (isFilled()) {
				
                g2d.fillOval(getX1(), getY1(), getX2(), getY2());
				
            }
			
            g2d.drawOval(getX1(), getY1(), getX2(), getY2());
			
            
			
        }
		
    }
	


 
}
