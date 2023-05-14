import { useEffect, useState } from 'react'
import styles from './CreateReadUpdateBook.module.css'
import { useLocation, useParams } from 'react-router'
import BookForm from '../../components/bookForm/BookForm'
import { AuthorDto, BookControllerApi, BookDto, BookOnShelveDto, GetBookByIdRequest, OfficeDto, PublisherDto, SubjectDto } from '../../../../shared/restApiClient';
let readOnly;
let title;
let buttonText;
let cancelButtonText;

const CreateReadUpdateBook = () => {
    const bookController = new BookControllerApi();
    const paramsLocation = useLocation();
    const paramsId = useParams();

    let [selectedBook, setSelectedBook] = useState<BookDto>();

    useEffect(()=>{
        const api =async () => {
            if(paramsId.id !== undefined){
                try {
                    const bookId:GetBookByIdRequest={
                        bookId : +paramsId.id
                    }
                    const data = await bookController.getBookById(bookId);
                    setSelectedBook(data)
                } catch (error) {
                    
                }

            }else{
                let publisher:PublisherDto={
                    id: 0,
                    name: ''
                }
                let office:OfficeDto={
                    id: 0,
                    name: '',
                    postalCode: '',
                    city: '',
                    street: '',
                    number: ''
                }
                let bookonShelve:BookOnShelveDto={
                    id: 0,
                    available: false
                }
                let subject:SubjectDto={
                    id: 0,
                    technologyName: ''
                }
                let subjects:SubjectDto[] = [];
                subjects.push(subject)
                let author:AuthorDto={
                    id: 0,
                    name: '',
                    firstName: ''
                }
                let authors:AuthorDto[]=[];
                authors.push(author)
                let book:BookDto = {
                    id: 0,
                    isbn: '',
                    title: '',
                    language: '',
                    pages: 0,
                    description: '',
                    publishedYear: 0,
                    publisherDto: publisher,
                    officeDto: office,
                    subjectDtos: [],
                    authorDtos: [],
                    bookOnShelveDto: bookonShelve
                };
                setSelectedBook(book)
                }
            }
        
        api();
    }, []);

    if(paramsLocation.pathname === '/books/add'){
        readOnly = false;
        title = 'Add book';
        buttonText = 'Add';
        cancelButtonText = 'Cancel'
    }else if(paramsLocation.pathname === '/books/edit/'+paramsId.id){
        readOnly = false;
        title = 'Update book';
        buttonText = 'Update';
        cancelButtonText = 'Cancel'
    }else{
        readOnly = true;
        title = 'Book details';
        buttonText = 'Ok';
        cancelButtonText = 'Return'
    }
  return (
     selectedBook ? <BookForm readOnly={readOnly} title={title} buttonText={buttonText} cancelButtonText={cancelButtonText} selectedBook={selectedBook}></BookForm> : <div className={styles['screen-title']}>Loading ...</div>
  )
}

export default CreateReadUpdateBook