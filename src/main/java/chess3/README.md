GameStatus
+ IN_PROGRESS
+ CHECKMATE
+ STALEMATE

PieceColor
+ WHITE
+ BLACK
+ opposite(): PieceColor

Move
- source: Square
- destination: Square
+ Move(source, destination)
+ getSource(): Square
+ getDestination(): Square

ChessGameFactory
- pieceFactory: PieceFactory
- playerFactory: PlayerFactory
+ ChessGameFactory(PlayerFactory, PieceFactory)
+ createPiece(PieceType, PieceColor): Piece
+ createPlayer(PieceColor, Square): Player

Game
- status: GameStatus
- turnColor: PieceColor
+ Game(boardSize)
- initialize(): void
- play(): void
- checkCondition(): void
- isCheckMate(): boolean
- isStaleMate(): boolean
- getNextMoveFromPlayer(): Move
- isValidMove(Move): boolean
- makeMove(Move): void
- switchTurn(): void

Board
- squares: Square[][]
- initialize(): void
+ Board(size)
- initialize(size)
+ isSquareOccupied(row, col, color): boolean
+ getPiece(row, col): Piece

Square
- row
- col
- piece: Piece
+ getRow(): int
+ getCol(): int
+ getPiece(): Piece
+ setPiece(piece: Piece): void

PieceType
+ KING
+ QUEEN
+ ROOK
+ KNIGHT
+ OUNT
+ PAWN

PieceFactory
+create(PieceType, PieceColor)

AbstractPiece
- color: PieceColor
+ abstract IsValidMove(board: Board, start: Square, end: Square): boolean
+ abstract IsUnderAttack(square: Square, position: Square): boolean

King(AbstractPiece)
Queen(AbstractPiece)
Rook(AbstractPiece)
Knight(AbstractPiece)
OUNT(AbstractPiece)
PAWN(AbstractPiece)

Player
- color: PieceColor
+ getPosition(): Square
