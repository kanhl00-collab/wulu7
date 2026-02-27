'''A game for fun!
In this game, you need to move your mouse to make the character collect blue
drops. Also, be careful to avoid the yellow bullets. Every 20 blue drops make a
level, and at a new level, while the score for each blue drop collected
increases, blue and yellow drops fall down at a higher speed. You have 3 lives
to begin, and 1 life = 100 blood. Everytime you miss a blue drop, you lose 10
blood. Everytime you get hit by a bullet, you lose 1 life. You can see your score,
level, life and how many bloods you have left during the game.'''


import pygame
from os import path
import random

#set the game screen
WIDTH=300
HEIGHT=500
FPS=30

#the directory to get the pictures for the game
img_dir=path.join(path.dirname(__file__))
'''pygame error:
if doesn't work, change to this:
img_dir=path.join(path.dirname(__file__),"GitHub/ICS3U/JAKA/Angel")'''

#define colors
BLACK=(0,0,0)
RED=(255,0,0)
GREEN=(0,255,0)
WHITE=(255,255,255)
BLUE=(0,0,255)
YELLOW=(255,255,0)

#set the font
font_name=pygame.font.match_font("arial")


#functions related with the update of the game are listed:

#function to display words at a certain position with a certain size
def draw_text(surf,text,size,x,y):
    font=pygame.font.Font(font_name,size)
    text_surface=font.render(text,True,WHITE)
    text_rect=text_surface.get_rect()
    text_rect.midtop=(x,y)
    surf.blit(text_surface,text_rect)
    
#create new drops
def newrain():
    r=Rain()
    all_sprites.add(r)
    drops.add(r)
    
#define pct and display the shield bar.
#Colour green shows how much blood remain within a life
def draw_shield_bar(surf,x,y,pct):
    if pct<0:
        pct=0
    BAR_LENGTH=100
    BAR_HEIGHT=10
    fill=(pct/100)*BAR_LENGTH
    outline_rect=pygame.Rect(x,y,BAR_LENGTH,BAR_HEIGHT)
    fill_rect=pygame.Rect(x,y,fill,BAR_HEIGHT)
    pygame.draw.rect(surf,GREEN,fill_rect)
    pygame.draw.rect(surf,WHITE,outline_rect,2)
    
#define heart and display
#set the img for a life, one heart represents one life
def draw_lives(surf,x,y,lives,img):
    for i in range(lives):
        img_rect=img.get_rect()
        img_rect.x= x + 30 * i
        img_rect.y= y
        surf.blit(img,img_rect)
        
#define start/end screen
#show instructions for the game every time a game starts or ends
def show_go_screen():
    screen.blit(background,background_rect)
    draw_text(screen,"Haaa",64,WIDTH/2,HEIGHT/4)
    draw_text(screen,"collect blue and avoid yellow",22,WIDTH/2,HEIGHT/2)
    draw_text(screen,"miss one blue drop = -10 blood",22,WIDTH/2,HEIGHT/2+25)
    draw_text(screen,"1 life = 100 blood",22,WIDTH/2,HEIGHT/2+50)
    draw_text(screen,"mouse to move",18,WIDTH/2,HEIGHT*3/4-20)
    draw_text(screen,"Press a key to begin",18,WIDTH/2,HEIGHT*3/4)
    pygame.display.flip()
    waiting=True
    while waiting:
        clock.tick(FPS)
        for event in pygame.event.get():
            if event.type==pygame.QUIT:
                pygame.quit()
            if event.type==pygame.KEYUP:
                waiting=False
                
#define rain and bullet
#add 20 new drops for a new level
#for each drop, there is 10% that a bullet appears
def level_up():
        for i in range(20):
                newrain()
                if random.random()<0.1:
                    b=Bullet()
                    all_sprites.add(b)
                    bullets.add(b)
            
#functions related with objects are listed:

#define player and update
class Player(pygame.sprite.Sprite):
    def __init__(self):
        pygame.sprite.Sprite.__init__(self)
        self.image=pygame.transform.scale(player_img,(50,50))#set img,size
        self.image.set_colorkey(BLACK)
        self.rect=self.image.get_rect()
        self.radius=25
        #pygame.draw.circle(self.image,RED,self.rect.center,self.radius)
        self.rect.centerx=WIDTH/2 #set horizontal position
        self.rect.bottom =HEIGHT-10 #set vertical position
        self.speedx=0 #set initial horizontal speed
        self.shield=100 #set initial blood
        self.lives=3 #set initial life
        
    def update(self):
        self.speedx=0

        pos=pygame.mouse.get_pos()
        self.rect.midtop=pos #img moves with the mouse
        '''if press key to move, code is here:
        keystate=pygame.key.get_pressed()
        if keystate[pygame.K_LEFT]:
            self.speedx=-10
        if keystate[pygame.K_RIGHT]:
            self.speedx=10'''

        '''self.rect.x+=self.speedx'''

       #make the img always stay in the screen
        if self.rect.right>WIDTH:
            self.rect.right=WIDTH
        if self.rect.left<0:
            self.rect.left=0
        if self.rect.bottom>HEIGHT:
            self.rect.bottom=HEIGHT
        if self.rect.top<450:
            self.rect.top=450


    
#define rain and update        
class Rain(pygame.sprite.Sprite):
    def __init__(self):
        pygame.sprite.Sprite.__init__(self)
        self.image=pygame.Surface((10,20))#set size
        self.image.fill(BLUE)#set color
        self.rect=self.image.get_rect()
        self.radius=int(self.rect.width/2)
        #pygame.draw.circle(self.image, BLUE,self.rect.center,self.radius)
        self.rect.x=random.randrange((WIDTH-self.rect.width)/10)*10#appear at random position
        self.rect.y=random.randrange(-100,0)*30#appear at different time
        self.speedy=level#speed increases as the level goes up
      
    def update(self):
        
        self.rect.y+=self.speedy#update position
        #delete the drops that goes outside of the screen or the player misses
        if self.rect.bottom>HEIGHT:
            self.kill()
            player.shield-=10#reduce remaining blood
            
#define bullet and update
class Bullet(pygame.sprite.Sprite):
    def __init__(self):
        pygame.sprite.Sprite.__init__(self)
        self.image=pygame.Surface((10,20))#set size
        self.image.fill(YELLOW)#set color
        self.rect=self.image.get_rect()
        self.radius=int(self.rect.width/2)
        self.rect.bottom=random.randrange(-2000,0)#random vertical position
        self.rect.centerx=random.randrange((WIDTH-self.rect.width)/10)*10#random horizontal position
        self.speedy=level#increase the speed as the level inceases
    def update(self):
        self.rect.y+=self.speedy#update the position of the bullet
        if self.rect.bottom>HEIGHT:#delete the avoided bullets
            self.kill()


#initialize pygame and create window
pygame.init()
pygame.mixer.init()
screen=pygame.display.set_mode((WIDTH,HEIGHT))#create a screen
pygame.display.set_caption("My Game")#caption of the window
clock=pygame.time.Clock()#update screen

#load all game graphics
background=pygame.image.load(path.join(img_dir,"bg.png")).convert()#set background img
background_rect=background.get_rect()
player_img=pygame.image.load(path.join(img_dir,"p1_stand.png")).convert()#set player img
life_img=pygame.image.load(path.join(img_dir,"hud_heartFull.png")).convert()#set heart img
life_img.set_colorkey(BLACK)
life_img=pygame.transform.scale(life_img,(25,19))#set size of heart


#Game loop
running=True
game_over=True
fall=True
while running:
    if game_over:
        #initialize the game
        show_go_screen()
        game_over=False
        all_sprites=pygame.sprite.Group()
        drops=pygame.sprite.Group()
        bullets=pygame.sprite.Group()
        player=Player()
        all_sprites.add(player)
        score=0
        level=1
    if fall:
            level_up()
            fall=False#pause after 1 turn, wait for next level to fall
    if len(drops)==0:#start another turn
            level+=1#level up
            fall=True
            
                      
    clock.tick(FPS)#update frenquency
    #process input(events)
    for event in pygame.event.get():
        if event.type==pygame.QUIT:#end game
            running=False
        
    #update
    all_sprites.update()

    #check to see if a bullet hits the player
    #delete the bullet if hits
    hits=pygame.sprite.spritecollide(player,bullets,True,pygame.sprite.collide_circle)
    if hits:
        player.lives-=1#reudce 1 life

    #check to see if a drop hits the player
    #delete the drop if hits
    hits=pygame.sprite.spritecollide(player,drops,True,pygame.sprite.collide_circle)
    for hit in hits:
        score+=level#gain score
        
    #-1 live    
    if player.shield<=0:#if run out of bloods
        player.lives-=1#reduce 1 life
        player.shield=100#reset blood for a new life
        
    #game over
    if player.lives==0:#if run out of lives
        game_over=True

    #display screen and function
    screen.fill(BLACK)
    screen.blit(background,background_rect)
    all_sprites.draw(screen)
    draw_text(screen,str(score),18,WIDTH/2,10)
    draw_text(screen,"level"+str(level),18,WIDTH/2,30)
    draw_shield_bar(screen,5,5,player.shield)
    draw_lives(screen,WIDTH-100,5,player.lives,life_img)
    pygame.display.flip()
    
pygame.quit()
    

