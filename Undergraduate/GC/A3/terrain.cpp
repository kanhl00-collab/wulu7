
#ifdef __APPLE__
#  define GL_SILENCE_DEPRECATION
#  include <OpenGL/gl.h>
#  include <OpenGL/glu.h>
#  include <GLUT/glut.h>
#else
#  include <GL/gl.h>
#  include <GL/glu.h>
#  include <GL/freeglut.h>
#endif
#include <stdio.h>
#include <stdlib.h>
#include <stdio.h>
#include <stdlib.h>
# include <vector>
# include <iostream>
#include <cmath>
#include <cstdlib>
#include <ctime>
using namespace std;

int size;
//const int size = input;

//float height[size][size];
//float fnormal[size][size][3];
//float vnormal[size][size][3];

float angle = 50;
float amp;
GLdouble eye[3];
GLdouble lookAt[] = { 0, 0, 0 };
GLdouble up[] = { 0, 1, 0 };


vector<vector<float> > height;//(size, vector<float> (size,0));
vector<vector<vector<float> > > fnormal;//(size, vector<vector<float> > (size, vector<float> (3,0)));
vector<vector<vector<float> > > vnormal;//(size, vector<vector<float> > (size, vector<float> (3,0)));

//float** height = new float*[size];

//float*** fnormal = new float**[size];

int variation = 5;
int circleSize = 10;

bool quad = true;
int wire;
//bool circle = true;
int algo;
bool light = false;
bool shading = false;

float minHeight;
float maxHeight;

float light_pos[4] = {50,50,20,1};
float light_pos2[4] = {-50,-50,20,1};

float amb[4] = {1,1,1,1};
float diff[4] = {0.5,0.5,0.5,1};
float spec[4] = {1,1,1,1};

float m_amb[4] = {0.25,0.2,0.07,1};
float m_diff[4] = {0.75,0.61,0.23,1};
float m_spec[4] = {0.63,0.56,0.37,1};
float shiny = 5.12;

float al;
float be;

void FPS(int val){
    glutPostRedisplay();
    glutTimerFunc(17, FPS, 0);
}

//circle algorithm
void circles() {
    srand(time(NULL));
    for (int k = 0; k < size*size/10; k++) {
        int randX = rand() %size;
        int randZ = rand() % size;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                float distance = sqrt((randX-i)*(randX-i)+(randZ-j)*(randZ-j));
                float pd = distance *2 / circleSize;
                if (fabs(pd) <= 1.0) {
                    height[i][j] += variation/2 + cos(pd*3.14)*variation/2;
                    
                }
            }
        }
    }
}

//fault algorithm
void fault() {
    srand(time(NULL));
    for (int k = 0; k < size*2; k++) {
        float v = static_cast<float>(rand());
        float a = sin(v);
        float b = cos(v);
        float d = sqrtf(size*size*2);
        float c =(static_cast<float>(rand())/ RAND_MAX) *d -d/2;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                float distance = a*i + b*j -c;
                if (distance > 0) {
                    height[i][j] += 1;
                } else{
                    height[i][j] -= 1;
                }
            }
        }
    }
}

//midpoint displacement
void MDP(int ax, int ay, int bx, int by, float c) {
    //cout << ax << ay << bx << by << c << endl;
    int len = bx-ax;
    if (len < 2)
      return;
    //srand(time(NULL));
    //for (int k = 0; k < size*2; k++) {
        float d =(static_cast<float>(rand())/ RAND_MAX) *c -c/2;
        height[len/2][len/2] = (height[ax][ay]+height[bx-1][by-1]+height[ax][by-1]+height[bx-1][ay])/4+d;
    //}

        height[ax][ay+len/2] = (height[ax][ay]+height[ax][by-1]+2*height[len/2][len/2])/4
        + (static_cast<float>(rand())/ RAND_MAX) *c -c/2;

        height[ax+len/2][by-1] = (height[ax][by-1]+height[bx-1][by-1]+2*height[len/2][len/2])/4
        + (static_cast<float>(rand())/ RAND_MAX) *c -c/2;

        height[bx-1][ay+len/2] = (height[bx-1][by-1]+height[bx-1][ay]+2*height[len/2][len/2])/4
        + (static_cast<float>(rand())/ RAND_MAX) *c -c/2;

        height[ax+len/2][ay] = (height[ax][ay]+height[bx-1][ay]+2*height[len/2][len/2])/4
        + (static_cast<float>(rand())/ RAND_MAX) *c -c/2;



        MDP(ax,ay+len/2,ax+len/2,by,c*pow(2,-0.5));
        MDP(ax+len/2,ay,bx,ay+len/2,c*pow(2,-0.5));
        MDP(ax+len/2,ay+len/2,bx,by,c*pow(2,-0.5));
        MDP(ax,ay,ax+len/2,ay+len/2,c*pow(2,-0.5));
}

void MDP(){
    srand(time(NULL));
    MDP(0,0,size,size,50);
}

// calculate face normals
void fnormals() {

    for (int i = 0; i < size-1; i++) {
        for (int j = 0; j < size-1; j++) {
            float x1 = 1;
            float y1 = height[i+1][j] - height[i][j];
            float z1 = 0;
            float x2 = 0;
            float y2 = height[i+1][j+1] - height[i+1][j];
            float z2 = 1;
            float dx = y1 * z2 - z1*y2;
            float dy = z1*x2 -x1*z2;
            float dz = x1*y2 -y1*x1;
            float length = sqrtf(dx*dx+dy*dy+dz*dz);
            fnormal[i][j][0] = dx/length;
            fnormal[i][j][1] = dy/length;
            fnormal[i][j][2] = dz/length;
        }
    }
}

//calculate vector normrals
void vnormals() {
    fnormals();
    for (int i = 1; i < size; i++) {
        for (int j = 1; j < size; j++) {
            float x = fnormal[i-1][j-1][0] + fnormal[i-1][j][0] + fnormal[i][j-1][0] + fnormal[i][j][0];
            float y = fnormal[i-1][j-1][1] + fnormal[i-1][j][1] + fnormal[i][j-1][1] + fnormal[i][j][1];
            float z = fnormal[i-1][j-1][2] + fnormal[i-1][j][2] + fnormal[i][j-1][2] + fnormal[i][j][2];
            float l = sqrtf(x*x + y*y + z*z);
            vnormal[i][j][0] = x/l;
            vnormal[i][j][1] = y/l;
            vnormal[i][j][2] = z/l;
        }
    }

}

// initialize size, arrays, eye
void init(){
//    cout << "how large?";
    //cin >> size;
    if (size > 300)
        size = 300;
    if (size < 50)
        size = 50;

    for (int i = 0; i < size; i++) {
        vector<float> heights(size, 0);
        height.push_back(heights);
    }

    for (int i = 0; i < size; i++) {
        vector<vector<float> > normals(size, vector<float> (3,0));
        fnormal.push_back(normals);
    }

    for (int i = 0; i < size; i++) {
        vector<vector<float> > normals(size, vector<float> (3,0));
        vnormal.push_back(normals);
    }

    amp = size;
    eye[0] = amp*cos(angle);
    eye[1] = amp;
    eye[2] = amp*sin(angle) ;
}

//    for (int i = 0; i < size; i++){
//        height[i] = new float[size];
//    }
    //for (int i = 0; i < size; i++) {
    //    for (int j = 0; j < size; j++) {
    //        height[i][j] = 0;
    //    }
    //}
//    for (int i = 0; i < size; i++) {
//        fnormal[i] = new float*[size];
//        for (int j = 0; j < size; j++) {
//            fnormal[i][j] = new float[3];
    //        fnormal[i][j][0] = 0;
    //        fnormal[i][j][1] = 0;
    //        fnormal[i][j][2] = 0;
//        }
//    }
//}


// generate height map
void generateHeight() {
    for (int i = 0; i < size; i++) {
        for (int j = 0; j < size; j++) {
            height[i][j] = 0;
        }
    }



    if (algo==0) {
        circles();
    } else if (algo == 1){
        fault();
    } else {
        MDP();
    }

    vnormals();


}

// find min and max height
void minAndMax(){
    for (int i = 0; i < size; i++){
        for (int j = 0; j < size; j++) {
            if (height[i][j] < minHeight)
                minHeight = height[i][j];
            else if (height[i][j] > maxHeight) {
                maxHeight = height[i][j];
            }
        }
    }

}

//lighting
void lightF() {
    //if (light) {
        glLightfv(GL_LIGHT0, GL_POSITION, light_pos);
        glLightfv(GL_LIGHT0, GL_DIFFUSE, diff);
        glLightfv(GL_LIGHT0, GL_AMBIENT, amb);
        glLightfv(GL_LIGHT0, GL_SPECULAR, spec);

        glLightfv(GL_LIGHT1, GL_POSITION, light_pos2);
        glLightfv(GL_LIGHT1, GL_DIFFUSE, diff);
        glLightfv(GL_LIGHT1, GL_AMBIENT, amb);
        glLightfv(GL_LIGHT1, GL_SPECULAR, spec);

        glMaterialfv(GL_FRONT_AND_BACK, GL_SPECULAR, m_spec);
        glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT, m_amb);
        glMaterialfv(GL_FRONT_AND_BACK, GL_DIFFUSE, m_diff);
        glMaterialf(GL_FRONT_AND_BACK, GL_SHININESS, shiny);

    //}
}

// draw solid terrain
void drawTerrain(){

    glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);

    for (int j = 0; j<size-1; j++) {
        if (quad)
            glBegin(GL_QUAD_STRIP);
        else {
            glBegin(GL_TRIANGLE_STRIP);
        }

        for (int i = 0; i < size-1; i++) {
            //glEdgeFlag(GL_FALSE);
            float grey = (height[i][j] - minHeight) / (maxHeight - minHeight);
            //if (light) {
            //    lightF();
            //} else {
            glColor3f(grey,grey,grey);
            //}
            //if (i%2 == 0 && j%2 == 0)
            if (!shading)
                glNormal3f(vnormal[i][j][0],vnormal[i][j][1],vnormal[i][j][2]);
            else {
                glNormal3f(fnormal[i][j][0],fnormal[i][j][1],fnormal[i][j][2]);
            }
            glVertex3f(i-size/2,height[i][j],j-size/2);
            grey = (height[i][j+1] - minHeight) / (maxHeight - minHeight);
            //if (light) {
            //    lightF();
            //} else {
                glColor3f(grey,grey,grey);
            //}
            if (!shading)
                glNormal3f(vnormal[i][j+1][0],vnormal[i][j+1][1],vnormal[i][j+1][2]);
            //else {
            //    glNormal3fv(fnormal[i][j+1]);
            //}
            glVertex3f(i-size/2,height[i][j+1],(j+1)-size/2);
            grey = (height[i+1][j] - minHeight) / (maxHeight - minHeight);
            glColor3f(grey,grey,grey);
            if (!shading)
                glNormal3f(vnormal[i+1][j][0],vnormal[i+1][j][1],vnormal[i+1][j][2]);
            glVertex3f(i+1-size/2,height[i+1][j],j-size/2);
            grey = (height[i+1][j+1] - minHeight) / (maxHeight - minHeight);
            glColor3f(grey,grey,grey);
            if (!shading)
                glNormal3f(vnormal[i+1][j+1][0],vnormal[i+1][j+1][1],vnormal[i+1][j+1][2]);
            glVertex3f(i+1-size/2,height[i+1][j+1],(j+1)-size/2);
        }

        glEnd();
    }
}

// draw wireframe
void drawTerrainW(){

    glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
    //if (! light)
        glColor3f(0,1,0);
    //else {

    //}


    for (int j = 0; j<size-1; j++) {
        if (quad)
            glBegin(GL_QUAD_STRIP);
        else {
            glBegin(GL_TRIANGLE_STRIP);
        }

        for (int i = 0; i < size; i++) {
            //glEdgeFlag(GL_FALSE);
            glNormal3f(vnormal[i][j][0],vnormal[i][j][1],vnormal[i][j][2]);
            glVertex3f(i-size/2,height[i][j],j-size/2);
            glNormal3f(vnormal[i][j][0],vnormal[i][j][1],vnormal[i][j][2]);
            glVertex3f(i-size/2,height[i][j+1],(j+1)-size/2);
        }

        glEnd();
    }
}


//draw terrain
void draw() {
    minAndMax();

    if (light) {
        lightF();
    }
    if (wire == 1) {
        drawTerrainW();
    } else if (wire == 2) {
        drawTerrain();
        glEnable(GL_POLYGON_OFFSET_LINE);
        glPolygonOffset(-1.0,-1.0);
        //glDisable(GL_DEPTH_TEST);
        drawTerrainW();
        //glEnable(GL_DEPTH_TEST);
        glDisable(GL_POLYGON_OFFSET_LINE);
    } else {
        drawTerrain();
    }
}


void display(void) {
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    glLoadIdentity();
    gluLookAt(
        eye[0], eye[1], eye[2],
        lookAt[0], lookAt[1], lookAt[2],
        up[0], up[1], up[2]
    );


    glRotatef(al,1,0,0);
    glRotatef(be,0,0,1);

    draw();





    glFlush();

    glutPostRedisplay();
}

void special(int key, int x, int y)
{
    /* arrow key presses move the camera */
    /************************************************************************

                        CAMERA CONTROLS

     ************************************************************************/
    switch(key)
    {
        case GLUT_KEY_DOWN:
            eye[1] += 1;

            break;
        case GLUT_KEY_UP:
            if (eye[1] > 0)
                eye[1] -= 1;

            break;
        case GLUT_KEY_LEFT:
            angle += 0.01;
            eye[0] = amp*cos(angle);
            eye[2] = amp*sin(angle);
            break;
        case GLUT_KEY_RIGHT:
            angle -= 0.01;
            eye[0] = amp*cos(angle);
            eye[2] = amp*sin(angle);
            break;


    }
    glutPostRedisplay();
}

void handleReshape(int w, int h) {
    glViewport(0, 0, w, h);
    glMatrixMode(GL_PROJECTION);
    glLoadIdentity();
    /**
     * Call gluPerspective here.
     */
    gluPerspective(45, 1, 1, 1000);
    //glOrtho(-30,30,-20,20,-100,200);

    glMatrixMode(GL_MODELVIEW);
}

void handleKeyboard(unsigned char key, int _x, int _y) {
    if (key == 'q') {
        exit(0);
    }
    switch(key){
        case 'R':
        case 'r':
          generateHeight();
          break;
        case 'S':
        case 's':
            quad = !quad;
            break;
        case 'W':
        case 'w':
            wire ++;
            wire %= 3;
            break;
        case 'A':
        case 'a':
            algo ++;
            algo %= 3;
            //circle = !circle;
            break;
        case 'L':
        case 'l':
            if (! light) {
                glEnable(GL_LIGHTING);
                glEnable(GL_LIGHT0);
                glEnable(GL_LIGHT1);
                light = true;
            }
            else {
                glDisable(GL_LIGHTING);
                glDisable(GL_LIGHT0);
                glDisable(GL_LIGHT1);
                light = false;
            }
            break;
        case 'F':
        case 'f':
            if (shading) {
                shading = false;
                //glShadeModel(GL_SMOOTH);
            } else {
                shading = true;
                //glShadeModel(GL_FLAT);
            }
            break;
        //case '+':
        //    size+=10;
        //    break;
        //case '-':
        //    size-=10;
        //    break;

            case 'e':
                light_pos[0]++;
                break;
            case 't':
                light_pos[0]--;
                break;
            case 'y':
                light_pos[1]++;
                break;
            case 'u':
                light_pos[1]--;
                break;
            case 'i':
                light_pos[2]++;
                break;
            case 'g':
                light_pos[2]--;
                break;

            case 'h':
                light_pos2[0]++;
                break;
            case 'j':
                light_pos2[0]--;
                break;
            case 'k':
                light_pos2[1]++;
                break;
            case 'b':
                light_pos2[1]--;
                break;
            case 'o':
                light_pos2[2]++;
                break;
            case 'p':
                light_pos2[2]--;
                break;
        case 'Z':
        case 'z':
            amp*=0.99;
            eye[0] = amp*cos(angle);
            eye[1] *= 0.99;
            eye[2] = amp*sin(angle);
            break;
        case 'X':
        case 'x':
            amp*=1.01;
            eye[0] = amp*cos(angle);
            eye[1] *= 1.01;
            eye[2] = amp*sin(angle);
            break;
        case 'C':
        case 'c':
            if (al < 90)
                al++;
            break;
        case 'V':
        case 'v':
            if (al > -90)
                al--;
            break;
        case 'N':
        case 'n':
            if (be < 90)
                be++;
            break;
        case 'M':
        case 'm':
            if (be > -90)
                be--;
            break;

    }


}

void mouse(int button, int state, int x, int y){
    if (button == GLUT_LEFT_BUTTON && state == GLUT_DOWN){
        std::cout << "x: " << x << " y: " << y << std::endl;
        //std::cout << height[x-300][y-300] << std::endl;
    }
}

int main(int argc, char** argv) {
    cout << "size:";
    cin >> size;
    init();

    
    //int size;
    //aA();
    cout << "Left and right arrow kayes to rotate around y axis" << endl;
    cout << "Up and down to move camera up and down" << endl;
    cout << "C and V to rotate around x axis" << endl;
    cout << "N and M to rotate around z axis" << endl;
    cout << "Z and X to zoom in and out" << endl;
    cout << "W to toggle between solid, wireframe, and both" << endl;
    cout << "L to enable lighting" << endl;
    cout << "R to regenerate terrain" << endl;
    cout << "S to toggle between triangle strips and quad strips" << endl;
    cout << "A to toggle between circle, fault and midpoint displacement algorithm. Aftering changing algorithm, remember to hit R to apply the new algorithm!!" << endl;
    cout << "F to toggle between flat shading and Gouraud shading in lighting mode" << endl;
    cout << "e, t, y, u, i, g to move light1 position" << endl;
    cout << "h, j, k, b, o, p to move light2 position" << endl;
    cout << "Q to quit" << endl;



    glutInit(&argc, argv);
    glutInitWindowSize(600,600);
    glutInitWindowPosition(300,300);
    glutInitDisplayMode(GLUT_RGB | GLUT_DEPTH);
    glutCreateWindow("Terrain");


    generateHeight();

    glutKeyboardFunc(handleKeyboard);
    //glutMouseFunc(mouse);
    glutReshapeFunc(handleReshape);
    glutDisplayFunc(display);
    glutSpecialFunc(special);
    //glutTimerFunc(17, FPS, 0);

    glEnable(GL_DEPTH_TEST);
    glEnable(GL_CULL_FACE);
    glFrontFace(GL_CCW);
    glCullFace(GL_BACK);



    glutMainLoop();


    //for (int i = 0; i < size; i++){
    //    delete[] height[i];
    //}
    //delete[] height;
    //for (int i = 0; i < size; i++){
    //    for (int j = 0; j < size; j++) {
    //        delete[] fnormal[i][j];
    //    }
    //    delete[] fnormal[i];
    //}
    //delete[] fnormal;

    return 0;
}
