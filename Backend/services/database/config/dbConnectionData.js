// MongoDB Atlas URI
const dbUser = process.env.DB_USER;
const dbPwd = process.env.DB_PWD;
const dbHost = process.env.DB_HOST;
export const mongoConnectionUrl = `mongodb+srv://${dbUser}:${dbPwd}@${dbHost}/?retryWrites=true&w=majority&appName=Cluster0`;