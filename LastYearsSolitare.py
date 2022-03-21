# imports
from random import shuffle
import pygame

# Pygame initalization
pygame.init()
scr = pygame.display.set_mode((555, 600))
pygame.display.set_caption("Solitaire")

clock = pygame.time.Clock()

# Assets
card_font = pygame.font.SysFont('Cantarell', 20, True, False)
emoji_font = pygame.font.SysFont('twemoji', 1, False, False) # twemoji font (twemoji.twitter.com)
bg = pygame.image.load('Background.png') # icons from twemoji (twemoji.twitter.com)

class Card:
    # Class representing one card
    cards = [] # a list of all created cards

    # A list of all cases (to be used with foundation)
    cases = [
        'clubs',
        'spades',
        'hearts',
        'diamonds'
    ]

    # Icons to be subsituted for the names on the cards
    icons = {
        'clubs': '♣',
        'spades': '♠',
        'hearts': '♥',
        'diamonds': '♦'
    }

    def __init__(self, case, number):
        Card.cards.append(self)

        # Set case and color
        self.case = case
        if case == 'spades' or case == 'clubs':
            self.color = 'black'
            self.col_num = 1
        else:
            self.color = 'red'
            self.col_num = 0

        # Save number, 1 corresponds to an ace, 11 is jack and so on
        self.number = number

        # Ensure card is face down
        self.revealed = False

# Card management
def make_deck():
    """
    Make a shuffled deck of cards
    :param None
    :return list of all 52 cards in a random order, all face down
    """
    deck = []
    # Create cards
    for case in ['hearts', 'diamonds', 'spades', 'clubs']:
        for i in range(1, 14):
            deck.append(Card(case, i))

    shuffle(deck)
    return(deck)

def deal(deck):
    """
    Deal the given deck, dividing the given deck into all necessary piles
    :param deck to deal from
    :return modified version of the deck
    :return a list of card stacks (represented as their own lists) that represents the tableau (table where most of the game takes place)
    """
    tableau = []
    for stack in range(7):
        new_stack = []
        for card in range(stack + 1):
            new_stack.append(deck.pop())
        new_stack[-1].revealed = True
        tableau.append(new_stack)

    return(deck, tableau)

def flip_deck(deck, flipped):
    """
    Flip the draw deck by three and return cards to draw deck when necessary
    :param deck to flip from
    :param stack to put cards into
    :return None
    """
    if len(deck) > 0:
        for i in range(3): # Flip and reveal three cards
            try:
                flipped.append(deck.pop())
                flipped[-1].revealed = True
            except:
                break
    else: # Return cards to deck once all cards have been flipped
        for card in reversed(flipped):
            deck.append(card)
            deck[-1].revealed = False
        flipped = []

    # Set message
    global message
    message = "Good Move!"

    return flipped

def good_move(card, dest):
    """
    Check if move is legal
    :param card that it being moved
    :param destination of said card
    :return True if move is legal, False otherwise
    """
    # Rules for cards being moved
    if not card[0].revealed: # Ensure card is face up
        return False

    print(dest)
    # Rules for destination: tableau
    if dest[0] == 'tableau':
        if len(tableau[dest[1]]) == 0: # If a non-king is played against a blank space
            if card[0].number != 13:
                return False
            else: 
                return True
        if tableau[dest[1]][-1].number != card[0].number + 1: 
            return False
        if tableau[dest[1]][-1].color == card[0].color: # If the card and top of the dest are the same color
            return False

    # Rules for destination: foundation
    if dest[0] == 'foundations':
        print("Card Case:", card[0].case, "Pile Case:", Card.cases[dest[1]])
        if card[0].case != Card.cases[dest[1]]: # If case does not match the foundation's case
            return False
        if len(foundations[dest[1]]) == 0: # If the stack is empty, the card must be an ace
            if card[0].number != 1:
                return False
            else:
                return True
        if foundations[dest[1]][-1].number != card[0].number - 1: # Card must be one more then the top of the dest
            return False
        # If the card is coming from the tableau, it must be on the top of a stack as cards can 
        # only be added to the foundation one at a time.
        if card[1][0] == "tableau":
            if card[0] != tableau[card[1][1]][-1]:
                return False

    # If we have gotten this far, the move must be good
    return True

def move(card, dest):
    """
    Move a card to a new location (is always initiated by a users click)
    :param card to move
    :param destination of the card (in the form of a stack)
    """
    # Ensure Move is Legal
    if not good_move(card, dest):
        global message
        message = "Bad Move :("
        return None

    moving = [] # where to store cards when being moved

    # Check if card is in the deck
    if card[1][0] == "deck":
        moving.append(deck_flipped.pop())

    # Check if card is in tableau
    elif card[1][0] == "tableau":
        # Add the selected card and all cards above it to moving list to be moved
        for i in range(card[1][2], len(tableau[card[1][1]])):
            moving.append(tableau[card[1][1]].pop(card[1][2]))
        if len(tableau[card[1][1]]) > 0:
            tableau[card[1][1]][-1].revealed = True

    # Check if card is in foundations
    elif card[1][0] == "foundations":
        moving.append(foundations[card[1][1]].pop())

    # Add cards to dest
    if dest[0] == "tableau":
        tableau[dest[1]].extend(moving)
    else:
        foundations[dest[1]].extend(moving)

    message = "Good Move!"

def check_win(founds):
    """
    Check if the player has won
    :param Foundations to check
    :return True if the player has won, False otherwise
    """
    for pile in founds:
        if len(pile) == 0 or pile[-1].number != 13:
            return False
    return True

# Mouse management
collides = lambda a,b,c: a[0] >= b[0] and a[0] <= b[0]+c[0] and a[1] >= b[1] and a[1] <= b[1]+c[1] # intersect logic in a one-liner

def clicked(previous_clicked):
    """
    Figure out where the mouse has clicked and do the correct action
    :param previous card clicked on (only used for moving cards in the tableau, otherwise False)
    :return card clicked
    """
    mouse_pos = pygame.mouse.get_pos()
    print(mouse_pos)

    selected = (None, None)

    # Draw Pile
    if collides(mouse_pos, (20, 20), (65, 100)):
        global deck_flipped
        deck_flipped = flip_deck(deck, deck_flipped)
        return None
    elif collides(mouse_pos, (95, 20), (65, 100)):
        if len(deck_flipped) > 0:
            selected = (deck_flipped[-1], ("deck", -1)) # I don't need to check if something has already been 
            return selected                             # selected as you can't move cards /to/ the draw pile.
        else:
            return None

    # Tableau
    for pile in range(len(tableau)):
        if collides(mouse_pos, (20 + pile*75, 140), (65, 100 + 20 * (len(tableau[pile]) - 1))):
            for card in range(len(tableau[pile]) - 1):
                if mouse_pos[1] <= 140 + 20*(card + 1):
                    #print("Tableau", pile, "Card", card)
                    selected = ((tableau[pile][card], ("tableau", pile, card)), ("tableau", pile))
                    break
            else:
                #print("Tableau", pile, "Card", len(tableau[pile]) - 1)
                if len(tableau[pile]) > 0:
                    selected = ((tableau[pile][-1], ("tableau", pile, len(tableau[pile]) - 1)), ("tableau", pile))
                else:
                    selected = (None, ("tableau", pile))

    # Foundations
    for pile in range(len(foundations)):
        if collides(mouse_pos, (245 + 75*pile, 20), (65, 100)):
            #print("Foundation", pile)
            if len(foundations[pile]) > 0:
                selected = ((foundations[pile][-1], ("foundations", pile)), ("foundations", pile))
            else:
                selected = (None, ("foundations", pile))

    if previous_clicked and selected[1] != None:
        move(previous_clicked, selected[1])
    else:
        return selected[0]

# Drawing functions
def draw_stack(pos, stack):
    """
    Draw a singular card
    :param position of card to draw
    :param card to draw
    :return None
    """
    if len(stack) > 0: # Draw stack
        card = stack[-1]
        if card.revealed: # Draw front
            pygame.draw.rect(scr, (255, 255, 255), (pos[0], pos[1], 65, 100), border_radius = 3)
            pygame.draw.rect(scr, (200, 200, 200), (pos[0], pos[1], 65, 100), width = 2, border_radius = 3)
            label = card_font.render(str(card.number), True, card.color)
            symbol = emoji_font.render(Card.icons[card.case], True, card.color)
            scr.blit(label, (pos[0], pos[1]))
            scr.blit(symbol, (pos[0] + 2, pos[1] + 5))
        else: # Draw back
            pygame.draw.rect(scr, (0, 0, 0), (pos[0], pos[1], 65, 100), border_radius = 3)
            pygame.draw.rect(scr, (40, 40, 40), (pos[0], pos[1], 65, 100), width = 2, border_radius = 3)
    else: # Draw space for stack
        pygame.draw.rect(scr, (0, 0, 0), (pos[0], pos[1], 65, 100), border_radius = 3, width = 2)

def draw_tab(pos, stack):
    """
    Draw a stack of the tableau
    :param position where to draw
    :param stack to draw
    :return None
    """
    for card in range(len(stack)):
        draw_stack((pos[0], pos[1] + card*20), [stack[card]])

def draw():
    """
    Draw the board with pygame
    :param none
    :return none
    """
    # Background
    scr.blit(bg, (0, 0))

    # Draw Deck
    draw_stack((20, 20), deck)
    draw_stack((95, 20), deck_flipped)

    # Draw Foundation
    for foundation in range(len(foundations)):
        draw_stack((245 + 75*foundation, 20), foundations[foundation])

    # Draw Tableau
    for pile in range(len(tableau)):
        draw_tab((20 + pile*75, 140), tableau[pile])

    # Draw Message
    message_rendered = card_font.render(message, True, "black")
    scr.blit(message_rendered, (0, 570))

    # Draw 
    pygame.display.update()

# Board Setup
message = "Begin"
# Create each stack
deck = make_deck()
deck_flipped = []
foundations = [ [], [], [], [] ]

# Deal the deck
deck, tableau = deal(deck)

run = True
last_clicked = False
while run:
    clock.tick(24)

    # Close when asked
    for event in pygame.event.get():
        if event.type == pygame.QUIT:
            run = False
        elif event.type == pygame.MOUSEBUTTONUP:
            last_clicked = clicked(last_clicked)

    if check_win(foundations):
        print("You Win!!")
        break

    draw()

pygame.quit()
