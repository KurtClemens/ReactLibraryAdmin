import Cards from '../../../../components/card/Cards'
import { useState, useEffect } from 'react'
import { UserControllerApi, UserDto } from '../../../../shared/restApiClient'

type resultProps=UserDto

const UserCard = () => {
  const userController = new UserControllerApi();
  const [result, setResult] = useState<resultProps[]>([]);

  useEffect(()=>{
    const api = async () => {
      const data = await userController.getAllUsers();
      setResult(data);
    };
  
    api();
  }, []);

  return (
    <Cards title={"Users"}>{result.length} users are added to the application</Cards>
  )
}

export default UserCard