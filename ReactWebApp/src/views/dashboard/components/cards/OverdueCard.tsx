import { useEffect, useState } from 'react'
import Cards from '../../../../components/card/Cards'
import { LoanControllerApi, LoanDto } from '../../../../shared/restApiClient';

type resultProps = LoanDto;

const OverdueCard = () => {
  const loanController = new LoanControllerApi();
  const [result, setResult] = useState<resultProps[]>([]);
  const [cardContent, setCardContent] = useState<string>('');


  useEffect(() => {
    const api = async () => {
      const data = await loanController.getLoansOverdue();
      setResult(data);
      if(data.length === 1){
        setCardContent('book is overdue')
      }else{
        setCardContent('books are overdue')
      }
    };

    api();
  }, []);


  return (
    <Cards title={"Loans"}>{result.length} {cardContent}</Cards>
  )
}

export default OverdueCard