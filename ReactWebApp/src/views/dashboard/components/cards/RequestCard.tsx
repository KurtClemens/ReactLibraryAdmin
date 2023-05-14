import { useEffect, useState } from 'react'
import Cards from '../../../../components/card/Cards'
import { BookRequestControllerApi, BookRequestDto } from '../../../../shared/restApiClient'

type resultProps = BookRequestDto;

const RequestCard = () => {
  const bookRequestController = new BookRequestControllerApi();
  const [result, setResult] = useState<resultProps[]>([]);

  useEffect(() => {
    const api = async () => {
      const data = await bookRequestController.getAllBookRequests();
      let newRequests:BookRequestDto[] = [];
      data.forEach((element: BookRequestDto) => {
        if (!element.mailSend) {
          newRequests.push(element)
        }
      });
      setResult(newRequests);
    };

    api();
  }, []);

  return (
    <Cards title={"Requests"}>There are {result.length} new book requests</Cards>
  )
}

export default RequestCard