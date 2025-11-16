class Book:
    def __init__(self,BookID,BookTitle,AuthorName,status="Available"):
        self.BookID=BookID
        self.BookTitle = BookTitle
        self.AuthorName=AuthorName
        self.status=status
        self.next=None
class bookList:
    def __init__(self):
        self.head=None
    def insertBook(self,BookID,BookTitle,AuthorName):
        new_book=Book(BookID,BookTitle,AuthorName)
        if not self.head:
            self.head=new_book
        else:
            temp=self.head
            while temp.next:
                temp=temp.next
            temp.next = new_book
        print(f"Book '{BookTitle}' added successfully!")
    def deleteBook(self,BookID):
        temp=self.head
        prev=None
        while temp and temp.BookID!=BookID:
            prev=temp
            temp=temp.next
        if not temp:
            print("Book not found.")
            return
        if not prev:
            self.head=temp.next
        else:
            prev.next=temp.next
        print(f"Book '{temp.BookTitle}' deleted successfully!")
    def searchBook(self, book_id):
        temp = self.head
        while temp:
            if temp.BookID==BookID:
                print(f"Book Found:")
                print(f"  ID: {temp.BookID}")
                print(f"  Title: {temp.BookTitle}")
                print(f"  Author: {temp.AuthorName}")
                print(f"  Status: {temp.status}")
                return temp
            temp=temp.next
        print("Book not found.")
        return None
    def displayBooks(self):
        if not self.head:
            print("No books available in the library.")
            return
        print("Current Library Books:")
        print("-"*25)
        temp=self.head
        while temp:
            print(f"ID:{temp.BookID}|Title:{temp.BookTitle}|Author:{temp.AuthorName}|Status:{temp.status}")
            temp = temp.next
        print("-"*25)
class TransactionStack:
    def __init__(self):
        self.stack=[]
    def push(self,transaction):
        self.stack.append(transaction)
    def pop(self):
        if not self.is_empty():
            return self.stack.pop()
        return None
    def is_empty(self):
        return len(self.stack) == 0
    def viewTransactions(self):
        if self.is_empty():
            print("No transactions yet.")
            return
        print("\nTransaction History:")
        print("-"*25)
        for t in reversed(self.stack):
            print(f"Book ID: {t['BookID']} | Action: {t['Action']}")
        print("-"*15)
class LibrarySystem:
    def __init__(self):
        self.BookList=BookList()
        self.TransactionStack=TransactionStack()
    def issueBook(self,BookID):
        book=self.book_list.searchBook(BookID)
        if book and book.status =="Available":
            book.status = "Issued"
            self.transaction_stack.push({"BookID":BookID,"Action":"ISSUE"})
            print(f"Book '{book.BookTitle}' issued successfully!")
        elif book:
            print("Book already issued.")
    def returnBook(self,BookID):
        book=self.bookList.searchBook(BookID)
        if book and book.status=="Issued":
            book.status="Available"
            self.transactionStack.push({"BookID":BookID,"Action":"RETURN"})
            print(f"Book '{book.BookTitle}' returned successfully!")
        elif book:
            print("Book is already available.")
    def undoTransaction(self):
        last=self.transactionStack.pop()
        if not last:
            print("No transactions to undo.")
            return
        book = self.bookList.searchBook(last["BookID"])
        if not book:
            print("Book record not found for undo.")
            return
        if last["Action"]=="ISSUE":
            book.status ="Available"
            print(f"Undo Successful: Book '{book.BookTitle}' marked as Available again.")
        elif last["Action"]=="RETURN":
            book.status="Issued"
            print(f"Undo Successful: Book '{book.BookTitle}' marked as Issued again.")
def main():
    lib=LibrarySystem()
    while True:
        print("\n==============================")
        print(" LIBRARY BOOK MANAGEMENT MENU")
        print("==============================")
        print("1. Insert Book")
        print("2. Delete Book")
        print("3. Search Book")
        print("4. Display Books")
        print("5. Issue Book")
        print("6. Return Book")
        print("7. Undo Last Transaction")
        print("8. View All Transactions")
        print("9. Exit")

        choice = input("Enter your choice: ")

        if choice == "1":
            try:
                BookID=int(input("Enter Book ID: "))
                BookTitle = input("Enter Book Title: ")
                AuthorName = input("Enter Author Name: ")
                lib.bookList.insert_book(BookID,BookTitle,AuthorName)
            except ValueError:
                print("Invalid input. Try again.")
        elif choice == "2":
            BookID=int(input("Enter Book ID to delete: "))
            lib.bookList.deleteBook(BookID)
        elif choice == "3":
            BookID=int(input("Enter Book ID to search: "))
            lib.bookList.searchBook(BookID)
        elif choice == "4":
            lib.bookList.displayBooks()
        elif choice == "5":
            BookID= int(input("Enter Book ID to issue: "))
            lib.issueBook(BookID)
        elif choice == "6":
            BookID= int(input("Enter Book ID to return: "))
            lib.returnBook(BookID)
        elif choice == "7":
            lib.undoTransaction()
        elif choice == "8":
            lib.transaction_stack.viewTransactions()
        elif choice == "9":
            print("Exiting Library System. Goodbye!")
            break
        else:
            print("Invalid choice. Please try again.")
if __name__ == "__main__":
    main()
