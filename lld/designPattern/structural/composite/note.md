like some tree having hirearichal structure.
ex: filessystem

file class- name, size, content, open
folder class - name, listoffile, listoffolder

ls() - list directory

composite design pattern -- composite and leaf should have common interface
here for file and folder class

fileSystemItem - interface with method - ls() openAll() getSize() getName()

and both implement this

folder is a **And** has a fileSystemItem similar to decorator

file is a filesystemItem
9