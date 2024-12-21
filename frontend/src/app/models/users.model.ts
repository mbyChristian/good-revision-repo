export interface Register{
  firstName: string;
  lastName: string;
  username: string;
  email: string;
  password: string;
  confirmPassword: string;
}

export interface Login{
  username:string;
  password:string

}
export interface User {
  firstName: string;
  lastName: string;
  role:string
}


export interface AuthResponse {
  token: string;
  type: string;
  user: User;
}

