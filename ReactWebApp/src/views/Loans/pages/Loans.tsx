import styles from './Loans.module.css'
import LoanTable from '../components/loanTable/LoanTable';
import Layout from '../../layout/Layout';

const Loans = () => {

  return (
    <>
      <Layout>
      <div className={styles['screen-title']}>Loans</div>
      <LoanTable ></LoanTable>
        </Layout>
    </>
  )
}

export default Loans