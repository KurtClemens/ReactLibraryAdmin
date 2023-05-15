import styles from './Loans.module.css'
import LoanTable from '../components/loanTable/LoanTable';

const Loans = () => {

  return (
    <>
      <div className={styles['screen-title']}>Loans</div>
      <LoanTable ></LoanTable>
    </>
  )
}

export default Loans