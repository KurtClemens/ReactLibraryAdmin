import { useEffect, useState } from 'react'
import Cards from '../../../../components/card/Cards'
import { BookControllerApi, BookDto,  } from '../../../../shared/restApiClient'

type resultProps = BookDto;

const BooksCard = () => {
  const bookController = new BookControllerApi();
  const [result, setResult] = useState<resultProps[]>([]);
  const [cardContent, setCardContent] = useState<string>('');

  useEffect(() => {
    const api = async () => {
      try{
        const data = await bookController.getAllBooks();
        setResult(data);
        if (data.length > 1) {
          setCardContent(`There are ${data.length} books`)
        } else if (data.length === 1) {
          setCardContent(`There is ${data.length} book`)
        }
      }catch{
        setCardContent('There are no books')
      }    
      
    };

    api();
  },);

  return (
    <Cards title={"Books"}>{cardContent}</Cards>
  )
}

export default BooksCard