// MongoDB Atlas URI. En caso de no recibir los valores se mostraran mensajes indicando que hay problemas con .env
const dbUser = process.env.DB_USER || 'error process.env.DB_USER';
const dbPwd = process.env.DB_PWD || 'error process.env.DB_PWD';
const dbHost = process.env.DB_HOST || 'error process.env.DB_HOST';
export const mongoConnectionUrl = `mongodb+srv://${dbUser}:${dbPwd}@${dbHost}/?retryWrites=true&w=majority&appName=Cluster0`;